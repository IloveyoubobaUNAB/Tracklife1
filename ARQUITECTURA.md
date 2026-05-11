# Informe de Arquitectura — ViveBien (TrackLife)
**Repositorio:** https://github.com/MarcozVD/Tracklife  
**Upstream (original):** https://github.com/IloveyoubobaUNAB/Tracklife  
**Fecha:** Mayo 2026

---

## 1. Partes y Módulos de la Aplicación

### Módulos lógicos (Monolito Modular)

```
app/src/main/java/com/jpalomino502/vivebien/
│
├── core/                          ← Compartido por todos los features
│   ├── data/local/                ← Room: AppDatabase, DAOs, Entities
│   ├── di/                        ← AppModule, DatabaseModule (Hilt)
│   ├── domain/model/              ← Modelos de dominio puros
│   └── ui/components/             ← Composables reutilizables
│
├── feature/                       ← Un módulo lógico por funcionalidad
│   ├── auth/                      ← Autenticación
│   │   ├── data/                  ← AuthRepository, AuthRepositoryImpl
│   │   ├── domain/usecase/        ← LoginUseCase, RegisterUseCase
│   │   └── ui/                    ← LoginViewModel, RegisterViewModel
│   ├── activity/                  ← Registro de actividad física
│   │   ├── data/                  ← ActivityRepository, ActivityRepositoryImpl
│   │   ├── domain/usecase/        ← GetTodayActivities, AddActivity, ToggleActivity, GetDailyStats
│   │   └── ui/                    ← ActivityViewModel
│   ├── health/                    ← Signos vitales y medicación
│   │   ├── data/                  ← HealthRepository, HealthRepositoryImpl
│   │   ├── domain/model/          ← HeartRateData, BloodPressureData
│   │   ├── domain/usecase/        ← AddVitalSign, GetHeartRate, GetBloodPressure, Medications
│   │   └── ui/                    ← HealthViewModel
│   ├── appointments/              ← Gestión de citas médicas
│   │   ├── data/                  ← AppointmentsRepository, AppointmentsRepositoryImpl
│   │   ├── domain/usecase/        ← GetAppointments, AddAppointment, DeleteAppointment
│   │   └── ui/                    ← AppointmentsViewModel
│   ├── profile/                   ← Perfil de usuario y ajustes
│   │   ├── data/                  ← ProfileRepository, ProfileRepositoryImpl
│   │   ├── domain/usecase/        ← GetUserProfile, UpdateProfile, Settings, Logout
│   │   └── ui/                    ← ProfileViewModel
│   └── home/                      ← Dashboard principal
│       ├── data/                  ← HomeRepository, HomeRepositoryImpl
│       ├── domain/usecase/        ← GetDashboard
│       └── ui/                    ← HomeViewModel
│
├── navigation/                    ← Screen, ScreenHome, NavGraph
└── ui/screens/                    ← Composables de pantalla (Presentation)
```

### Tecnologías y librerías utilizadas

| Categoría | Librería / Framework | Versión |
|-----------|---------------------|---------|
| UI | Jetpack Compose + Material 3 | BOM 2024.x |
| Navegación | Navigation Compose | 2.7.x |
| DI | Hilt (Dagger) | 2.51.1 |
| Procesador DI | KSP | 2.0.0-1.0.21 |
| Base de datos | Room | 2.6.1 |
| Preferencias | DataStore Preferences | 1.1.1 |
| Estado reactivo | Kotlin StateFlow / Flow | Coroutines 1.7.x |
| Ciclo de vida | lifecycle-runtime-compose | 2.7.x |
| ViewModel | lifecycle-viewmodel-compose | 2.7.x |
| Lenguaje | Kotlin | 2.0.0 |
| Build | Gradle KTS + Version Catalogs | 8.x |

---

## 2. Repositorio — Evidencia del Estado Inicial

**URL fork:** https://github.com/MarcozVD/Tracklife  
**Branch:** `main`  
**Commit inicial (app base):** `345c736` — "Create app vivebien" (21/03/2025)

### Estructura original (arquitectura inicial — Big Ball of Mud)

```
vivebien/
├── MainActivity.kt              ← @AndroidEntryPoint sin DI configurado
├── navigation/
│   ├── NavGraph.kt              ← Screen sealed class inline + lógica de navegación
│   └── ScreenHome.kt
├── ui/
│   ├── components/BottomBar.kt
│   ├── screens/
│   │   ├── LoginScreen.kt       ← Lógica de auth directamente en el Composable
│   │   ├── HomeScreen.kt        ← Datos hardcodeados inline
│   │   ├── SaludScreen.kt       ← Datos hardcodeados, 2 tabs vacíos
│   │   ├── ActividadScreen.kt   ← Datos hardcodeados, sin interacción
│   │   ├── CitasScreen.kt       ← Calendario estático, sin persistencia
│   │   └── PerfilScreen.kt      ← Datos hardcodeados
│   └── theme/
└── utils/
    └── Constants.kt             ← Credenciales en texto plano (TEST_USER, TEST_PASSWORD)
```

**Problemas identificados en la arquitectura inicial:**
- Sin separación de capas: UI, lógica y datos en el mismo archivo
- Sin ViewModels: estado gestionado directamente en Composables con `remember`
- Sin inyección de dependencias configurada (Hilt declarado pero no usado)
- Datos completamente hardcodeados, sin persistencia
- Credenciales en texto plano en `Constants.kt`
- Duplicación masiva: 4 implementaciones idénticas de `TabRow`
- Sin modelos de dominio (entidades de negocio inexistentes)
- Sin patrón Repository ni UseCases
- Navegación con sealed class definida inline en el mismo archivo donde se usa

---

## 3. Historial de Commits — Evolución de la Arquitectura

### Commit 1 — `555cdfc` | 11/05/2026
**"feat: agregar Hilt, Room y DataStore al sistema de build"**  
*6 archivos cambiados, 43 inserciones*

**Cambios:** `gradle/libs.versions.toml`, `build.gradle.kts`, `app/build.gradle.kts`  
**Motivo:** La app no tenía DI funcional ni persistencia. Se agregaron las dependencias base requeridas por la nueva arquitectura: Hilt 2.51.1 con KSP (compatible con Kotlin 2.0.0, kapt está obsoleto), Room 2.6.1 y DataStore 1.1.1. Sin estas dependencias ningún paso posterior era posible.

---

### Commit 2 — `6fd140a` | 11/05/2026
**"feat: agregar modelos de dominio y módulo de inyección de dependencias (Hilt)"**  
*6 archivos cambiados, 85 inserciones*

**Cambios:** `ViveBienApp.kt`, `AndroidManifest.xml`, `core/domain/model/*.kt`, `core/di/AppModule.kt`  
**Motivo:** Clean Architecture requiere modelos de dominio puros (sin dependencias de Android). Se crearon `User`, `ActivityRecord`, `Appointment`, `Medication`, `VitalSign`. Se registró `ViveBienApp` con `@HiltAndroidApp` para activar el grafo de dependencias. `AppModule` define los bindings de repositorios.

---

### Commit 3 — `f47d2b5` | 11/05/2026
**"feat: implementar capa de autenticación con MVVM y Clean Architecture"**  
*6 archivos cambiados, 160 inserciones, 28 eliminaciones*

**Cambios:** `AuthRepository`, `AuthRepositoryImpl`, `LoginUseCase`, `LoginViewModel`, `LoginScreen` (refactorizado)  
**Motivo:** La pantalla de login tenía lógica de negocio directamente en el Composable (acceso a `Constants.TEST_USER`). Se extrajo a capas separadas: el `UseCase` valida las reglas, el `Repository` abstrae el almacenamiento (DataStore), el `ViewModel` gestiona el estado con `StateFlow`. La pantalla quedó como observador puro del estado. Se eliminaron las credenciales hardcodeadas de `Constants.kt`.

---

### Commit 4 — `b9a65bd` | 11/05/2026
**"feat: crear ViewModels con StateFlow para cada feature de la aplicación"**  
*5 archivos cambiados, 247 inserciones*

**Cambios:** `HomeViewModel`, `HealthViewModel`, `ActivityViewModel`, `AppointmentsViewModel`, `ProfileViewModel`  
**Motivo:** Las 5 pantallas principales carecían de ViewModel, gestionando su "estado" con datos hardcodeados. Se crearon ViewModels con `@HiltViewModel` e `@Inject constructor` para cada feature, exponiendo `StateFlow<UiState>`. Esto establece el contrato UI ↔ ViewModel que permite reemplazar datos estáticos por datos reales en el siguiente paso.

---

### Commit 5 — `ffc6e44` | 11/05/2026
**"refactor: extraer componente CustomTabRow y separar definición de rutas"**  
*3 archivos cambiados, 65 inserciones, 12 eliminaciones*

**Cambios:** `CustomTabRow.kt` (nuevo), `Screen.kt` (nuevo), `NavGraph.kt` (limpiado)  
**Motivo:** Existían 4 implementaciones idénticas del componente `TabRow` copiadas en cada pantalla (duplicación DRY). Se extrajo a `CustomTabRow.kt` reutilizable. Adicionalmente, `NavGraph.kt` contenía una `sealed class Screen` inline junto a la lógica de navegación, violando separación de responsabilidades. Se separó en `Screen.kt` independiente.

---

### Commit 6 — `adb7f12` | 11/05/2026
**"feat: conectar todas las pantallas a sus ViewModels mediante StateFlow"**  
*5 archivos cambiados, 329 inserciones, 1.007 eliminaciones*

**Cambios:** `HomeScreen`, `SaludScreen`, `ActividadScreen`, `CitasScreen`, `PerfilScreen` refactorizados completamente  
**Motivo:** Las pantallas aún renderizaban datos hardcodeados. Se refactorizaron para usar `viewModel: XViewModel = hiltViewModel()` y `collectAsStateWithLifecycle()`. Se eliminaron 1.007 líneas de datos estáticos y se reemplazaron por observación reactiva del estado del ViewModel. El calendario pasó de filas hardcodeadas a cálculo dinámico con `java.time`.

---

### Commit 7 — `25dc8f3` | 11/05/2026
**"feat: agregar flujo de creación de cuenta (registro de usuario)"**  
*8 archivos cambiados, 306 inserciones, 9 eliminaciones*

**Cambios:** `RegisterScreen`, `RegisterViewModel`, `RegisterUseCase`, actualización de `AuthRepository`, `Screen.Register`, `NavGraph`  
**Motivo:** La app solo permitía login con credenciales fijas. Se implementó el flujo completo de registro siguiendo la misma arquitectura MVVM+Clean Architecture: `RegisterUseCase` con validaciones de negocio, `RegisterViewModel` con estado reactivo, `RegisterScreen` como vista pura, persistencia en DataStore, y enlace de navegación desde el LoginScreen.

---

### Commit 8 — `54a501d` | 11/05/2026
**"feat: hacer la app completamente funcional con datos reales (monolito modular)"**  
*58 archivos cambiados, 2.039 inserciones, 706 eliminaciones*

**Cambios:** Base de datos Room completa, 5 repositorios reales, 20 casos de uso, ViewModels actualizados, pantallas con diálogos interactivos  
**Motivo:** Los ViewModels aún retornaban datos hardcodeados dentro del ViewModel (simulación). Se implementó la capa de datos completa: 4 entidades Room con mappers, 4 DAOs con queries reactivos (Flow), AppDatabase, DatabaseModule. Cada feature obtuvo su propio repositorio que conecta Room/DataStore con el dominio. Los ViewModels pasaron a inyectar casos de uso reales. Las pantallas recibieron diálogos funcionales para crear/eliminar datos.

---

## 4. Modelo C4 — ANTES de los Cambios (Arquitectura Inicial)

### Nivel 1 — Contexto del Sistema

```
┌─────────────────────────────────────────────────────────┐
│                    CONTEXTO DEL SISTEMA                  │
└─────────────────────────────────────────────────────────┘

         ┌──────────┐
         │ Usuario  │
         │(Paciente)│
         └────┬─────┘
              │ Usa
              ▼
   ┌──────────────────────┐
   │     ViveBien App     │
   │   (Android - Kotlin) │
   │                      │
   │  Aplicación móvil de │
   │  seguimiento de salud│
   │  y bienestar personal│
   └──────────────────────┘
   
   Sin sistemas externos.
   Sin backend.
   Sin base de datos.
   Datos 100% hardcodeados.
```

---

### Nivel 2 — Contenedores (ANTES)

```
┌─────────────────────────────────────────────────────────────┐
│                   CONTENEDORES — ANTES                       │
└─────────────────────────────────────────────────────────────┘

   ┌───────────────────────────────────────────────────────┐
   │               Aplicación Android (APK)                 │
   │                                                         │
   │  ┌─────────────────────────────────────────────────┐  │
   │  │              UI Layer ÚNICO                      │  │
   │  │                                                   │  │
   │  │  LoginScreen.kt    ← lógica auth + UI + datos    │  │
   │  │  HomeScreen.kt     ← UI + datos hardcodeados     │  │
   │  │  SaludScreen.kt    ← UI + datos hardcodeados     │  │
   │  │  ActividadScreen.kt← UI + datos hardcodeados     │  │
   │  │  CitasScreen.kt    ← UI + datos hardcodeados     │  │
   │  │  PerfilScreen.kt   ← UI + datos hardcodeados     │  │
   │  │                                                   │  │
   │  │  Constants.kt      ← credenciales en texto plano │  │
   │  │  NavGraph.kt       ← nav + sealed class inline   │  │
   │  └─────────────────────────────────────────────────┘  │
   │                                                         │
   │  SIN: ViewModels, Repository, UseCase, Room, Hilt DI   │
   └───────────────────────────────────────────────────────┘

   Sin almacenamiento externo.
   Sin APIs.
```

---

### Nivel 3 — Componentes (ANTES)

```
┌─────────────────────────────────────────────────────────────┐
│              COMPONENTES — ANTES (Big Ball of Mud)           │
└─────────────────────────────────────────────────────────────┘

  MainActivity
      │
      └──► NavGraph ──► LoginScreen
                   ├──► MainScreen ──► BottomBar
                                  ├──► HomeScreen
                                  │      └── datos hardcodeados inline
                                  ├──► SaludScreen
                                  │      └── datos hardcodeados inline
                                  ├──► ActividadScreen
                                  │      └── datos hardcodeados inline
                                  ├──► CitasScreen
                                  │      └── datos hardcodeados inline
                                  └──► PerfilScreen
                                         └── datos hardcodeados inline

  ┌──────────────────────────────────────┐
  │           ANTIPATRONES               │
  │                                      │
  │  ✗ God Composable (todo en uno)      │
  │  ✗ Magic Numbers / Magic Strings     │
  │  ✗ Duplicación (TabRow x4)           │
  │  ✗ Credenciales hardcodeadas         │
  │  ✗ Sin separación de capas           │
  │  ✗ Sin estado gestionado             │
  └──────────────────────────────────────┘
```

---

## 5. Modelo C4 — DESPUÉS de los Cambios (Arquitectura Mejorada)

### Nivel 1 — Contexto del Sistema (DESPUÉS)

```
┌─────────────────────────────────────────────────────────────┐
│                    CONTEXTO DEL SISTEMA                      │
└─────────────────────────────────────────────────────────────┘

         ┌──────────┐
         │ Usuario  │
         │(Paciente)│
         └────┬─────┘
              │ Interactúa con
              ▼
   ┌──────────────────────────┐       ┌──────────────────┐
   │     ViveBien App         │       │  Almacenamiento  │
   │   (Android - Kotlin)     │──────►│    Local         │
   │                          │       │  Room DB         │
   │  Seguimiento de salud:   │       │  DataStore       │
   │  · Actividad física      │       └──────────────────┘
   │  · Signos vitales        │
   │  · Medicación            │
   │  · Citas médicas         │
   │  · Perfil personal       │
   └──────────────────────────┘
```

---

### Nivel 2 — Contenedores (DESPUÉS)

```
┌──────────────────────────────────────────────────────────────────┐
│                    CONTENEDORES — DESPUÉS                         │
└──────────────────────────────────────────────────────────────────┘

  ┌────────────────────────────────────────────────────────────┐
  │                   Aplicación Android (APK)                  │
  │                                                              │
  │  ┌────────────────┐   ┌────────────────┐  ┌─────────────┐ │
  │  │  Presentation  │   │    Domain      │  │    Data     │ │
  │  │    Layer       │──►│    Layer       │◄─│    Layer    │ │
  │  │                │   │                │  │             │ │
  │  │  Composables   │   │  UseCases      │  │ Repository  │ │
  │  │  ViewModels    │   │  Models        │  │ Room DB     │ │
  │  └────────────────┘   └────────────────┘  └─────────────┘ │
  │                                                              │
  │  ┌────────────────┐   ┌────────────────────────────────┐   │
  │  │     Core       │   │      Hilt DI Container         │   │
  │  │                │   │                                │   │
  │  │  AppDatabase   │   │  Singleton Scope               │   │
  │  │  DAOs          │   │  ViewModelComponent            │   │
  │  │  AppModule     │   │  SingletonComponent            │   │
  │  │  DatabaseModule│   └────────────────────────────────┘   │
  │  └────────────────┘                                         │
  └────────────────────────────────────────────────────────────┘

  ┌──────────────────────┐     ┌─────────────────────────┐
  │   Room Database      │     │  DataStore Preferences  │
  │  (vivebien_db)       │     │  (auth_prefs,           │
  │                      │     │   profile_prefs)        │
  │  · activities        │     │                         │
  │  · appointments      │     │  · is_logged_in         │
  │  · medications       │     │  · display_name         │
  │  · vital_signs       │     │  · notifications        │
  └──────────────────────┘     └─────────────────────────┘
```

---

### Nivel 3 — Componentes por Feature (DESPUÉS)

```
┌──────────────────────────────────────────────────────────────────┐
│               COMPONENTES — FEATURE ACTIVITY (ejemplo)            │
└──────────────────────────────────────────────────────────────────┘

  ┌─ PRESENTATION ─────────────────────────────────────────────┐
  │  ActividadScreen                                             │
  │    · collectAsStateWithLifecycle()                           │
  │    · AddActividadDialog (formulario nuevo)                   │
  │    · ActividadItem clickeable (toggle)                       │
  │    │                                                         │
  │    ▼  hiltViewModel()                                        │
  │  ActivityViewModel (@HiltViewModel)                          │
  │    · uiState: StateFlow<ActivityUiState>                     │
  │    · onToggleActivity(), onAddActivity(), onShow/HideDialog() │
  └─────────────────────────────────────────────────────────────┘
              │ @Inject constructor
              ▼
  ┌─ DOMAIN ───────────────────────────────────────────────────┐
  │  GetTodayActivitiesUseCase   → Flow<List<ActivityRecord>>   │
  │  GetDailyStatsUseCase        → Flow<DailyStats>             │
  │  AddActivityUseCase          → validación + persist         │
  │  ToggleActivityUseCase       → toggle completed             │
  │                                                             │
  │  ActivityRecord (domain model - sin dep. Android)           │
  │  DailyStats (domain model)                                  │
  └─────────────────────────────────────────────────────────────┘
              │ @Inject constructor
              ▼
  ┌─ DATA ─────────────────────────────────────────────────────┐
  │  ActivityRepository (interfaz)                               │
  │    └── ActivityRepositoryImpl (@Singleton)                   │
  │          · getTodayActivities(): Flow<List<ActivityRecord>>  │
  │          · getDailyStats(): Flow<DailyStats>                 │
  │          · addActivity(), toggleCompleted(), deleteActivity() │
  │                                                             │
  │  ActivityDao (@Dao — Room)                                   │
  │    · getByDateRange(start, end): Flow<List<ActivityEntity>>  │
  │    · insert(), update(), delete()                            │
  │                                                             │
  │  ActivityEntity (@Entity — Room)                             │
  │    · id, title, durationMinutes, distanceKm,                 │
  │      scheduledTime, completed, date                          │
  │    · toDomain() / toEntity() mappers                         │
  └─────────────────────────────────────────────────────────────┘
```

```
┌──────────────────────────────────────────────────────────────────┐
│               COMPONENTES — NIVEL SISTEMA COMPLETO               │
└──────────────────────────────────────────────────────────────────┘

  MainActivity (@AndroidEntryPoint)
       │
       └──► NavGraph
              ├──► LoginScreen ──► LoginViewModel ──► LoginUseCase
              │                                    └──► AuthRepository
              ├──► RegisterScreen ──► RegisterViewModel ──► RegisterUseCase
              └──► MainScreen (rootNavController)
                     ├── BottomBar
                     └── NavHost (inner)
                           ├──► HomeScreen ──► HomeViewModel ──► GetDashboardUseCase
                           │                                  └──► HomeRepository
                           │                                       (agrega auth + appts + activity)
                           ├──► SaludScreen ──► HealthViewModel ──► AddVitalSignUseCase
                           │                                     ├──► GetHeartRateUseCase
                           │                                     ├──► GetBloodPressureUseCase
                           │                                     └──► GetMedicationsUseCase
                           ├──► ActividadScreen ──► ActivityViewModel (ver diagrama anterior)
                           ├──► CitasScreen ──► AppointmentsViewModel ──► GetAppointmentsUseCase
                           │                                           ├──► AddAppointmentUseCase
                           │                                           └──► DeleteAppointmentUseCase
                           └──► PerfilScreen ──► ProfileViewModel ──► GetUserProfileUseCase
                                                                   ├──► UpdateProfileUseCase
                                                                   ├──► GetSettingsUseCase
                                                                   ├──► SaveSettingsUseCase
                                                                   └──► LogoutUseCase

  ┌─── HILT DI ────────────────────────────────────────────────────┐
  │  AppModule (abstract) → @Binds 6 repositorios                  │
  │  DatabaseModule (object) → @Provides AppDatabase + 4 DAOs      │
  └────────────────────────────────────────────────────────────────┘

  ┌─── ROOM DATABASE ──────────────────────────────────────────────┐
  │  AppDatabase (v1): activities, appointments, medications,       │
  │                    vital_signs                                  │
  └────────────────────────────────────────────────────────────────┘

  ┌─── DATASTORE ──────────────────────────────────────────────────┐
  │  auth_prefs: is_logged_in, registered_user, registered_pass,   │
  │              display_name                                       │
  │  profile_prefs: email, phone, birth_date, notifications,       │
  │                 dark_mode                                       │
  └────────────────────────────────────────────────────────────────┘
```

---

### Nivel 4 — Código: Flujo de Datos (DESPUÉS)

```
┌──────────────────────────────────────────────────────────────────┐
│         FLUJO DE DATOS — Nueva Medición de Presión Arterial       │
└──────────────────────────────────────────────────────────────────┘

  USUARIO toca "+ Nueva medición de presión"
       │
       ▼
  SaludScreen.onNuevaMedicionBp()
  → showBpDialog = true
       │
       ▼ El usuario ingresa sistólica=120, diastólica=80 y toca "Guardar"
       │
  HealthViewModel.onAddBloodPressure(120, 80)
       │
       ▼
  AddVitalSignUseCase.addBloodPressure(120, 80)
  · Valida: 60 ≤ 120 ≤ 300 ✓
  · Valida: 40 ≤ 80 ≤ 200 ✓
  · Valida: 80 < 120 (diast < sist) ✓
       │
       ▼
  HealthRepository.addVitalSign(VitalSign(BLOOD_PRESSURE_SYSTOLIC, 120f))
  HealthRepository.addVitalSign(VitalSign(BLOOD_PRESSURE_DIASTOLIC, 80f))
       │
       ▼
  VitalSignDao.insert(VitalSignEntity(type="BLOOD_PRESSURE_SYSTOLIC", value=120f))
  VitalSignDao.insert(VitalSignEntity(type="BLOOD_PRESSURE_DIASTOLIC", value=80f))
       │ Room emite el cambio
       ▼
  VitalSignDao.getLatestByType("BLOOD_PRESSURE_SYSTOLIC") → Flow emite nuevo valor
  VitalSignDao.getLatestByType("BLOOD_PRESSURE_DIASTOLIC") → Flow emite nuevo valor
       │ combine()
       ▼
  GetBloodPressureUseCase() → BloodPressureData(120, 80, "Normal", hasData=true)
       │
       ▼
  HealthViewModel._uiState.value = .copy(bloodPressure = BloodPressureData(...))
       │ StateFlow emite nuevo estado
       ▼
  SaludScreen recompone → muestra "120/80 · Normal"
```

---

## 6. Patrones de Diseño y Principios Aplicados

### Patrones aplicados

| Patrón | Dónde se aplica | Beneficio |
|--------|----------------|-----------|
| **MVVM** | Cada feature: Screen ↔ ViewModel ↔ Repository | Separación UI/lógica, estado reactivo |
| **Repository** | `AuthRepositoryImpl`, `ActivityRepositoryImpl`, etc. | Abstrae la fuente de datos (Room/DataStore) |
| **Use Case (Command)** | `LoginUseCase`, `AddActivityUseCase`, etc. | Encapsula reglas de negocio, reutilizable |
| **Observer** | `StateFlow` + `collectAsStateWithLifecycle()` | Reactividad, sin polling |
| **Factory (via DI)** | `@HiltViewModel`, `hiltViewModel()` | Creación controlada de ViewModels |
| **Singleton** | `@Singleton` en repositorios e `AppDatabase` | Una única instancia de DB y repositorios |
| **Mapper** | `toDomain()` / `toEntity()` en Entity files | Desacopla modelo de dominio del modelo de persistencia |
| **Strategy** | Interfaces de Repository con múltiples implementaciones posibles | Intercambiable (test/prod) |

### Principios SOLID aplicados

- **S (Single Responsibility):** Cada clase tiene una responsabilidad. `LoginUseCase` solo valida y autentica.
- **O (Open/Closed):** Los repositorios son interfaces; se puede agregar implementación remota sin modificar el ViewModel.
- **L (Liskov):** `ActivityRepositoryImpl` reemplaza completamente a `ActivityRepository` sin cambios en consumidores.
- **I (Interface Segregation):** `ProfileRepository`, `HealthRepository`, etc. exponen solo los métodos que su feature necesita.
- **D (Dependency Inversion):** ViewModels dependen de interfaces (UseCases → Repositories), no de implementaciones concretas.

### Clean Architecture — Regla de dependencias

```
  Presentation  →  Domain  ←  Data
  (ViewModels,      (UseCases,    (RepositoryImpl,
   Screens)          Models,       Room, DataStore)
                     Interfaces)

  La capa Domain no depende de ninguna capa externa.
  Las capas externas dependen hacia adentro.
```

---

## 7. Sustentación — Mejoras Implementadas

### Problema central resuelto
La aplicación original era un **"Big Ball of Mud"**: toda la lógica, los datos y la presentación convivían en los mismos archivos Composable. Era imposible testear, mantener o escalar.

### Mejoras por dimensión

**Mantenibilidad:**
- Antes: un bug en la lógica de citas implicaba modificar `CitasScreen.kt` (mezcla UI + lógica)
- Después: el bug está en `AddAppointmentUseCase.kt`. La pantalla no necesita cambiar.

**Escalabilidad:**
- Antes: agregar un nuevo feature significaba copiar y pegar el patrón de otra pantalla con todos sus problemas
- Después: el patrón `data/domain/ui` es consistente en todos los features. Agregar uno nuevo sigue la misma estructura.

**Testabilidad:**
- Antes: imposible hacer unit test (UI y lógica acopladas, sin interfaces)
- Después: `AddVitalSignUseCase` es testeable con un mock de `HealthRepository`. El `ViewModel` es testeable con un mock del `UseCase`.

**Seguridad:**
- Antes: credenciales en texto plano en `Constants.kt` (TEST_USER, TEST_PASSWORD)
- Después: credenciales gestionadas en DataStore cifrado, nunca en código fuente.

**Funcionalidad real:**
- Antes: 100% datos hardcodeados, app sin utilidad práctica
- Después: Room Database persiste todos los datos del usuario entre sesiones. Cualquier dato creado sobrevive al cierre de la app.

**Reactividad:**
- Antes: UI estática, nunca se actualiza
- Después: Flow de Room + StateFlow garantizan que la UI siempre refleja el estado actual de la base de datos automáticamente.

### Comparación cuantitativa

| Métrica | Antes | Después |
|---------|-------|---------|
| Archivos Kotlin | 13 | 69 |
| Capas de arquitectura | 1 (todo mezclado) | 3 (Presentation, Domain, Data) |
| Módulos lógicos | 0 | 6 (auth, activity, health, appointments, profile, home) |
| Datos persistidos | 0 | 4 tablas Room + 2 DataStores |
| Diálogos interactivos | 0 | 5 (BP, FC, actividad, cita, medicación) |
| Credenciales hardcodeadas | 2 | 0 |
| Líneas duplicadas (TabRow) | ~60 (x4) | 0 (componente único) |
| ViewModels | 0 | 7 |
| Casos de uso | 0 | 20 |
| Repositorios | 0 | 6 |

---

*Documento generado para la entrega de arquitectura móvil — ViveBien / TrackLife*
