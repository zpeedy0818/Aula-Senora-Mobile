# Aula Señora Mobile

Aplicación Android que conecta **estudiantes** con **voluntarios** para tutorías académicas. Los voluntarios crean aulas (cursos), los estudiantes solicitan acceso y, una vez aprobados, pueden agendar tutorías, chatear, compartir materiales y calificar la experiencia.

## Características

- **Tres roles de usuario**: Estudiante, Voluntario, Admin
- **Gestión de aulas**: Creación, edición, listado con búsqueda
- **Solicitudes de acceso**: Flujo de aprobación/rechazo por el voluntario
- **Tutorías**: Solicitud con fecha/hora, agenda de disponibilidad del voluntario, tutorías activas y próximas
- **Chat por aula**: Mensajes en tiempo real con indicador de no leídos
- **Materiales de apoyo**: Subida (voluntario) y descarga (estudiante) con conteo de vistas
- **Calificaciones**: Estudiantes califican al voluntario (1–5 estrellas) por aula
- **Notificaciones push**: Firebase Cloud Messaging para eventos importantes
- **Perfil de usuario**: Estadísticas de tiempo en la app, especialidad (voluntarios)
- **Estadísticas**: Gráficos con MPAndroidChart (admin)

## Tech Stack

| Componente        | Tecnología                                         |
| ----------------- | -------------------------------------------------- |
| Lenguaje          | Java 11                                            |
| Base de datos     | SQLite (local)                                     |
| UI                | Material Components, ConstraintLayout, ViewBinding |
| Notificaciones    | Firebase Cloud Messaging                           |
| Networking        | Retrofit + OkHttp + Gson                           |
| Gráficos          | MPAndroidChart                                     |
| Carga de imágenes | Glide                                              |
| API mínima        | 26 (Android 8.0)                                   |
| API objetivo      | 36 (Android 14)                                    |

## Arquitectura

App monolítica **sin backend remoto**. Toda la información persiste en SQLite local (`aulasenora.db`). Las notificaciones push llegan desde Firebase pero no hay sincronización externa.

### Roles

| Rol            | Acceso principal                                             |
| -------------- | ------------------------------------------------------------ |
| **Estudiante** | Dashboard con aulas admitidas, tutorías activas/próximas, chat, perfil |
| **Voluntario** | Gestión de aulas propias, solicitudes, agenda, materiales, chat, notificaciones |
| **Admin**      | Estadísticas globales, listado de usuarios                   |

### Base de datos (10 tablas, versión 15)

| Tabla                | Propósito                                                    |
| -------------------- | ------------------------------------------------------------ |
| `users`              | Usuarios registrados (nombre, email, rol, especialidad, tiempo en app) |
| `aulas`              | Aulas/cursos creados por voluntarios                         |
| `access_requests`    | Solicitudes de estudiantes para unirse a un aula             |
| `tutoring_requests`  | Solicitudes de tutoría con fecha, hora y tema                |
| `schedule_slots`     | Bloques de disponibilidad del voluntario                     |
| `support_materials`  | Archivos subidos por el voluntario                           |
| `material_downloads` | Registro de vistas por estudiante (único por material+estudiante) |
| `chat_messages`      | Mensajes de chat por aula                                    |
| `chat_read_status`   | Último mensaje leído por usuario por aula                    |
| `volunteer_ratings`  | Calificaciones de estudiantes a voluntarios (único por triplete voluntario+estudiante+aula) |

## Configuración y Build

### Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17+
- Gradle 9.4.1 (wrapper incluido)

### Compilar

```bash
./gradlew assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/`.

### Firebase (opcional)

Si se desea notificaciones push, agregar `google-services.json` en `app/` (descargado desde Firebase Console). Sin este archivo, la app compila y funciona sin notificaciones.

## Usuarios de prueba (seed data)

La app inserta estos usuarios automáticamente al iniciar por primera vez:

| Rol        | Email              | Contraseña    |
| ---------- | ------------------ | ------------- |
| Estudiante | `est@correo.com`   | `password123` |
| Voluntario | `vol@correo.com`   | `password123` |
| Admin      | `admin@correo.com` | `admin123`    |

## Estructura del proyecto

```
app/src/main/java/co/edu/aulasenora/
├── adapters/
│   └── TutorAdapter.java
├── api/
│   ├── ApiClient.java
│   └── RandomUserService.java
├── db/
│   └── DatabaseHelper.java
├── models/
│   ├── AccessRequest.java
│   ├── AdmittedStudent.java
│   ├── Aula.java
│   ├── ChatMessage.java
│   ├── NotificationItem.java
│   ├── RandomUserResponse.java
│   ├── ScheduleSlot.java
│   ├── SupportMaterial.java
│   ├── TutoringRequest.java
│   └── User.java
├── services/
│   ├── MyFirebaseMessagingService.java
│   └── NotificationHelper.java
├── HomeActivity.java           (launcher)
├── MainActivity.java            (login)
├── RoleSelectionActivity.java   (registro — selección de rol)
├── RegisterActivity.java        (registro — formulario)
├── StudentDashboardActivity.java
├── VolunteerDashboardActivity.java
├── AdminDashboardActivity.java
├── ManageAulasActivity.java     (listado de aulas — voluntario)
├── CreateAulaActivity.java
├── ManageAulaActivity.java      (detalle de aula — voluntario)
├── StudentAulaDetailActivity.java
├── ChatListActivity.java
├── ChatDetailActivity.java
├── NotificationsActivity.java
├── ScheduleManagementActivity.java
├── StatsActivity.java
└── ProfileActivity.java
```

## 
