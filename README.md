# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción

Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño de
Software. Un único proyecto Spring Boot (`compras-comportamiento`) que
resuelve cuatro necesidades reales del backend de ComprasUDES, el sistema
interno de solicitudes de compra corporativas: aprobación por niveles
jerárquicos, ejecución reversible de solicitudes aprobadas, notificaciones
ante cambios de estado y reglas de transición según el estado actual de la
solicitud.

## Cómo ejecutar

```
$ mvn clean package
$ mvn spring-boot:run
$ mvn test
```

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos

**Patrón aplicado: Chain of Responsibility**
(`NivelAprobacionHandler` y sus cuatro niveles concretos, orquestados desde
`CadenaAprobacionServicio`).

El síntoma del enunciado es exactamente el que describe el patrón: una
solicitud debe recorrer una secuencia de decisores independientes —
Revisor de Cumplimiento Normativo (solo para `INTERNACIONAL`), Supervisor
de Área, Gerente de Área, Director Financiero—, donde cada uno evalúa si
está dentro de su autoridad y, si no, delega al siguiente sin que el código
que dispara la evaluación (`ControladorSolicitudes`) sepa cuántos niveles
existen, en qué orden se consultan, ni si una categoría particular requiere
un nivel adicional. Cada `NivelAprobacionHandler` solo conoce al siguiente
eslabón, nunca a la cadena completa, lo que permite agregar el Revisor de
Cumplimiento (el cambio ya anunciado por el equipo de Compras) como un
nuevo eslabón al frente de la cadena sin tocar ningún nivel existente ni el
controlador.

Se descartó Command puro para esta necesidad porque Command resuelve un
problema distinto: convertir una acción en un objeto que pueda diferirse,
registrarse o revertirse. Aquí no hay ninguna operación que deba sobrevivir
como objeto para deshacerse más tarde —ese es exactamente el problema de la
Necesidad 2, no el de esta—; lo que hay es una pregunta de enrutamiento
("¿quién de la cadena debe resolver esta solicitud?") cuyo resultado se
produce y se usa en el mismo instante en que la cadena termina. Forzar
Command aquí habría significado encapsular cada nivel como una operación
reversible sin que exista nada que revertir, perdiendo además la semántica
de "evaluar condicionalmente y delegar" que es el corazón del problema.

### Necesidad 2 — Ejecución reversible de solicitudes aprobadas

**Patrón aplicado: Command**
(`OperacionSolicitud`, `ReservarPresupuestoCommand`,
`GenerarOrdenCompraCommand`, con `EjecutorSolicitud` como caretaker).

Reservar presupuesto y generar la orden de compra se modelan como objetos
Command independientes, cada uno con su propia lógica de `ejecutar()` y
`deshacer()` sobre `PresupuestoService` y `OrdenCompraService`
respectivamente, sin modificar ninguno de los dos. Esto permite deshacer
una operación sin afectar la otra, que es precisamente el requisito que
distingue a esta necesidad de un simple método con dos llamadas
secuenciales. A diferencia del undo/redo clásico de la lectura dirigida
—una pila donde solo se puede deshacer el último comando ejecutado—, el
enunciado exige poder deshacer cualquiera de las dos operaciones de forma
independiente de cuál se ejecutó después; por eso `EjecutorSolicitud`
conserva el historial como una lista direccionable (`deshacer(registro)`)
además de ofrecer `deshacerUltima()` como atajo para el caso más común, y
ese historial conserva **todas** las operaciones ejecutadas —deshechas o
no— para que cualquiera pueda inspeccionarse después, no solo la última.

Se descartó aquí el patrón de la Necesidad 1 (Chain of Responsibility): no
hay ningún decisor evaluando condiciones para decidir si delega o resuelve
una solicitud entrante. Reservar presupuesto y generar orden son dos
operaciones discretas que un mismo actor (el equipo de Compras) decide
ejecutar explícitamente, no una petición que avanza por una cadena de
responsables hasta que alguien la resuelve. Usar CoR aquí habría dejado sin
resolver el requisito central: ni la reversibilidad individual de cada
operación ni el historial consultable tienen un lugar natural en una cadena
de manejadores que se limita a decidir quién procesa una solicitud.

### Necesidad 3 — Notificaciones ante cambio de estado

**Patrón aplicado: Observer**
(`SuscriptorCambioEstado` y sus tres implementaciones, con
`NotificadorCambioEstado` como sujeto/publicador).

El problema no es el comportamiento propio de la `Solicitud`, sino la
reacción de tres módulos completamente ajenos a ella —correo, dashboard de
contabilidad, auditoría— que deben enterarse cuando el estado cambia, sin
que el código que produce ese cambio (en `CadenaAprobacionServicio` para
la Necesidad 1, en `EjecutorSolicitud` para la Necesidad 2) conozca a esos
tres módulos. `NotificadorCambioEstado` centraliza el cambio de estado y la
notificación: ambos puntos de la Necesidad 1 y la Necesidad 2 le delegan
`cambiarEstado(solicitud, nuevoEstado)` en lugar de llamar
`solicitud.setEstado(...)` directamente. Agregar una cuarta reacción —como
pide el enunciado para la prueba— solo exige escribir una clase que
implemente `SuscriptorCambioEstado` y registrarla con
`agregarSuscriptor()`, sin modificar `NotificadorCambioEstado` ni ninguna
de las tres reacciones existentes.

Esta necesidad se distingue de la Necesidad 4 (State) en el punto de
decisión 3 del enunciado: aquí la solicitud simplemente cambió de estado, y
son objetos externos quienes deben reaccionar, sin que la solicitud ni el
código que la modifica necesiten conocerlos. En la Necesidad 4, en cambio,
el comportamiento que cambia es el de la propia solicitud —qué operaciones
le están permitidas en cada momento—, no el de terceros reaccionando a un
hecho consumado. Confundir ambas habría significado, por ejemplo, intentar
resolver la lógica de "qué correo enviar" dentro de una jerarquía de
estados, o intentar resolver "qué operaciones son válidas ahora" con una
lista de suscriptores que no tiene ninguna noción de reglas de transición.

### Necesidad 4 — Reglas de transición según el estado actual

**Patrón aplicado: State**
(`EstadoSolicitud` y sus cinco estados concretos, con `ContextoSolicitud`
como contexto).

Cada operación válida (`aprobar`, `rechazar`, `ejecutar`, `cancelar`) se
delega al objeto que representa el estado actual de la solicitud
(`PendienteState`, `AprobadaState`, etc.), y es ese objeto concreto quien
decide, al resolver la operación, a qué estado siguiente transicionar
(`ContextoSolicitud.cambiarEstado`). Esto reemplaza el fragmento disperso
original —un `if/else` repetido con variaciones en varios métodos,
revisando `getEstado()` cada vez— por una única fuente de verdad: las
reglas de cada estado viven en su propia clase, y agregar un estado nuevo
(como `EN_ESPERA_PROVEEDOR`, ya anunciado) significa escribir una clase
nueva y registrarla en `EstadoSolicitudFactory`, sin tocar `ContextoSolicitud`
ni los demás estados ni volver a revisar cada método existente.

Se descartó aquí Strategy, el patrón de la guía con la estructura más
parecida (un contexto que delega en una interfaz implementada por varias
clases), por una razón de intención, no de forma: en Strategy, un cliente
externo elige e inyecta el comportamiento activo desde afuera en cada
llamada —como el carrito de compras de la guía activando la estrategia de
descuento que desea usar en ese momento—, y las estrategias concretas no se
conocen entre sí ni existe la noción de "transicionar" de una a otra. Aquí
no hay ningún cliente externo seleccionando entre operaciones
intercambiables: es la propia solicitud quien decide, según en qué estado
se encuentra en ese instante de su historia, qué operaciones admite, y
además debe poder pasar de un estado a otro como parte de resolver la
operación —`PendienteState.aprobar()` no solo ejecuta un cálculo, también
decide moverse a `AprobadaState`—, algo que un conjunto de comportamientos
independientes entre sí, por diseño, no hace por su cuenta. Modelar esta
necesidad con Strategy habría dejado sin resolver exactamente el problema
planteado: seguiría sin existir un lugar natural para decidir "después de
aprobar, ¿a qué estado pasamos?", porque Strategy no modela transiciones,
solo sustitución de algoritmos.

### Reflexión — otros tres patrones (opcional)

1. **Recorrer solicitudes de un centro de costo sin exponer la estructura
   de almacenamiento:** encaja **Iterator**. El reporte necesita recorrer
   secuencialmente una colección sin saber si internamente es una `List`,
   un `Map` o un árbol; Iterator expone un contrato uniforme (`hasNext()`,
   `next()`) que oculta esa estructura interna detrás de una interfaz
   común.

2. **Tres tipos de comprobante con el mismo esqueleto de impresión
   (encabezado, cuerpo, pie) que difieren solo en el cuerpo:** encaja
   **Template Method**. La clase base fijaría la secuencia completa
   (imprimir encabezado → delegar el cuerpo variable → imprimir pie) en un
   método final, y cada tipo de comprobante solo sobrescribiría el paso del
   cuerpo, igual que `JdbcTemplate` o `HttpServlet` en la lectura dirigida.

3. **Guardar y restaurar instantáneas completas del estado de una
   solicitud sin que el código que las guarda conozca sus detalles
   internos:** se acerca más **Memento** que el Command ya construido en la
   Necesidad 2. La diferencia es de qué se captura: cada Command de la
   Necesidad 2 encapsula una operación específica y su lógica particular
   de reversión (liberar presupuesto, cancelar una orden), mientras que
   Memento capturaría una fotografía completa e inmutable del estado
   interno de la solicitud en un momento dado, sin ninguna lógica de
   negocio asociada, para restaurarla tal cual más adelante —útil para
   "volver exactamente a como estaba todo a las 3pm", no para "deshacer la
   última acción que se tomó".

## Herramientas utilizadas

- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code o IntelliJ IDEA, Git, GitHub

## Conclusiones

Las cuatro necesidades de este laboratorio comparten un rasgo: en cada una,
el síntoma descrito encajaba, a primera vista, con más de un patrón de la
guía, y la decisión correcta dependía de una pregunta puntual más que de la
forma del diagrama de clases. Entre Chain of Responsibility y Command
(Necesidades 1 y 2), la pregunta fue si el problema era de enrutamiento
condicional o de reversibilidad de una operación ya decidida. Entre Observer
y State (Necesidades 3 y 4) —el par más sutil del laboratorio, junto con
State y Strategy dentro de la propia Necesidad 4—, la pregunta fue quién
cambia de comportamiento: terceros reaccionando a un hecho ya ocurrido, o el
propio objeto decidiendo su comportamiento válido según su historia. Lo más
difícil no fue escribir el código de cada patrón por separado, sino resistir
la tentación de aplicar el primero que "también funcionaría" sin verificar
que su intención coincidiera con el problema real, especialmente en la
Necesidad 4, donde Strategy comparte casi el mismo diagrama de clases que
State pero resuelve una pregunta de diseño completamente distinta.
