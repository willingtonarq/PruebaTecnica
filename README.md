# 🎬 CineReserve — Examen práctico de Programación Web 2

## 1. Descripción del proyecto

CineReserve es una aplicación web para consultar funciones de cine, visualizar la disponibilidad de asientos y realizar reservas.

El proyecto integra un backend desarrollado con **Java y Spring Boot** y un frontend desarrollado con **React, Vite y Axios**.

El objetivo del examen es completar, corregir y validar funcionalidades de una aplicación web que consume una API REST.

## 2. Arquitectura tecnológica

| Componente | Tecnología |
|---|---|
| Backend | Java 17 y Spring Boot |
| Gestión de dependencias | Maven Wrapper |
| API | REST |
| Frontend | React |
| Herramienta de desarrollo | Vite |
| Cliente HTTP | Axios |
| Pruebas frontend | Vitest y React Testing Library |
| Persistencia | En memoria |

## 3. Estructura del proyecto

```text
CineReserve-Examen/
├── cinereserve-api/
│   ├── src/
│   ├── pom.xml
│   └── mvnw
├── cinereserve-web/
│   ├── src/
│   │   ├── api/
│   │   ├── App.jsx
│   │   └── App.css
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## 4. Requisitos previos

El entorno de Codio debe disponer de:

- Java 21.
- Maven Wrapper incluido en el proyecto.
- Node.js 22 o una versión compatible con las dependencias del frontend.
- npm.
- Acceso a las terminales y a la vista previa de los puertos de Codio.

**Importante:** el backend y el frontend deben ejecutarse en terminales independientes.

## 5. Ejecución del backend

Abre una terminal y ejecuta:

```bash
cd ~/workspace/cinereserve-api
./mvnw spring-boot:run
```

El backend se ejecutará en el puerto `8080`.

Para ejecutar las pruebas del backend:

```bash
cd ~/workspace/cinereserve-api
./mvnw test
```

La ejecución correcta debe finalizar con:

```text
BUILD SUCCESS
```

## 6. Ejecución del frontend

Abre una segunda terminal:

```bash
cd ~/workspace/cinereserve-web
npm install
npm run dev -- --host 0.0.0.0
```

Vite se ejecutará en el puerto `5173`.

Abre la vista previa pública de ese puerto desde Codio para utilizar CineReserve.

La configuración de Vite debe conservar el proxy hacia Spring Boot y permitir el dominio de Codio utilizado por la actividad.

## 7. Configuración de Node.js y npm

Si la terminal no reconoce los comandos `node` o `npm`, puedes instalar NVM y Node.js 22.

### Instalar NVM

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.3/install.sh | bash
```

Cargar NVM en la terminal actual:

```bash
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
```

Instalar y activar Node.js 22:

```bash
nvm install 22
nvm use 22
```

Verificar las versiones:

```bash
node --version
npm --version
```

Si acabas de instalar NVM, abre una terminal nueva o vuelve a cargar el archivo `nvm.sh` cuando sea necesario.

## 8. Instalar Axios y las dependencias de pruebas

Si Axios no está instalado en el frontend:

```bash
cd ~/workspace/cinereserve-web
npm install axios
```

Si necesitas instalar las herramientas de pruebas de React:

```bash
npm install -D vitest jsdom @testing-library/react @testing-library/jest-dom @testing-library/user-event
```

Para ejecutar las pruebas:

```bash
npm test
```

El resultado mostrará el número de pruebas aprobadas y los posibles errores encontrados.

## 9. Funcionalidades del sistema

La aplicación permite:

1. Consultar las funciones de cine disponibles.
2. Visualizar película, horario y precio.
3. Consultar los asientos de una función.
4. Identificar los asientos disponibles y ocupados.
5. Seleccionar y deseleccionar asientos.
6. Ingresar el nombre del cliente.
7. Consultar el total según la cantidad de asientos seleccionados.
8. Confirmar una reserva.
9. Informar al usuario sobre el resultado de la operación.
10. Actualizar la disponibilidad después de una reserva.

## 10. Reglas de negocio

Las reservas deben respetar las siguientes condiciones:

- El nombre del cliente no puede estar vacío.
- Debe existir una función válida.
- Se debe seleccionar al menos un asiento.
- No se permiten códigos de asiento inexistentes.
- No se permite reservar un asiento ocupado.
- No se permite incluir el mismo asiento más de una vez en una solicitud.
- Una reserva inválida no debe dejar asientos parcialmente ocupados.
- Una reserva exitosa debe devolver la información de la reserva creada.

## 11. Evaluación automática

La actividad utiliza un evaluador automático de Codio con una calificación máxima de 100 puntos.

| Componente | Puntaje máximo |
|---|---:|
| Backend — Spring Boot y pruebas Maven | 60 puntos |
| Frontend — React y pruebas automatizadas | 40 puntos |
| **Total** | **100 puntos** |

El evaluador ejecuta las pruebas del backend y las pruebas oficiales de React configuradas para la actividad.

La calificación depende de los resultados obtenidos por cada bloque de evaluación. La ejecución local de las pruebas sirve para verificar el proyecto, pero no sustituye la calificación oficial de Codio.

## 12. Recomendaciones para el desarrollo

- Mantén el backend ejecutándose mientras pruebas el frontend.
- Utiliza las herramientas de desarrollo del navegador para revisar errores de React.
- Revisa la consola de Codio si una petición a la API falla.
- Comprueba las respuestas HTTP y el contenido JSON.
- Ejecuta las pruebas después de cada modificación importante.
- No elimines configuraciones de Vite, dependencias ni archivos del proyecto sin verificar su impacto.
- Recuerda que los datos se almacenan en memoria y pueden reiniciarse cuando se detenga la aplicación.

## 13. Entrega

Antes de marcar la actividad como completada:

- Verifica que el backend compile.
- Ejecuta las pruebas Maven.
- Verifica que React arranque correctamente.
- Comprueba el consumo de la API desde la interfaz.
- Prueba al menos una reserva exitosa y un caso de error.
- Revisa que el proyecto conserve la estructura requerida.

**¡Éxitos en el examen y buen desarrollo!**