# MaderiCheck

Aplicación móvil nativa en Android (Kotlin) para el control de asistencia de operarios en planta maderera, con registro local, sincronización en la nube mediante Xano y recursos nativos del dispositivo.

## Integrantes del Equipo
* **Cristóbal Abarca** (`Tobal-24`) - `cri.abarcaa@duocuc.cl`
* **Vicente Huenul** (`VicenteH-crypto`) - `vi.huenul@duocuc.cl`
* **Aaron Navarro** (`AaronNavarro1`) - `aar.navarro@duocuc.cl`

## Funcionalidades Principales
* **Registro e Inicio de Sesión:** Validación de credenciales y datos de operarios con persistencia local y respaldo remoto.
* **Control de Asistencia en Tiempo Real:** Reloj digital en vivo y registro de marcajes de **ENTRADA** y **SALIDA** por RUT.
* **Historial de Marcajes:** Visualización ordenada de asistencias con diseño Material Design y animaciones de transición.
* **Recursos Nativos Integrados:**
  * **Cámara (`TakePicturePreview`):** Escaneo rápido de credencial o código QR del operario con gestión dinámica de permisos.
  * **Notificaciones Locales (`NotificationManager`):** Alerta instantánea en la barra del sistema confirmando el tipo de marcaje y la hora exacta.
  * **Respuesta Háptica (`VibratorManager`):** Confirmación por vibración al validar o rechazar un marcaje.
* **Pruebas Unitarias (JUnit):** Validación automatizada de formularios y modelos de datos en `src/test`.

## Endpoints Consumidos (Retrofit + Xano)
* **Base URL:** `"https://x8ki-letl-twmt.n7.xano.io/api:n1NupKtK/"`
* `GET usuario` — Obtiene el listado de operarios registrados.
* `POST usuario` — Registra un nuevo operario en Xano.
* `GET usuario/{usuario_id}` — Consulta los datos de un operario específico.
* `DELETE usuario/{usuario_id}` — Elimina un registro de usuario.
* `GET asistencia` — Obtiene el historial global de marcajes.
* `POST asistencia` — Envía automáticamente cada marcaje de entrada o salida a Xano.
* `PATCH asistencia/{asistencia_id}` — Actualiza un registro de asistencia existente.
* `DELETE asistencia/{asistencia_id}` — Elimina un marcaje específico.
* `GET` (API Externa Open-Meteo) — Consulta condiciones climáticas externas mediante `@Url` dinámico.

## Pasos de Ejecución
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/Tobal-24/AlqaedaTeam.git](https://github.com/Tobal-24/AlqaedaTeam.git)
