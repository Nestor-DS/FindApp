<h1 align="center">FindApp 🗺️</h1>

![Logo de la Aplicación](URL_DEL_LOGO.png)

## Descripción

La Aplicación FindApp es una plataforma móvil desarrollada en Kotlin para dispositivos Android que permite la geolocalización de en tiempo real, facilitando la visualización de su ubicación en un mapa. De esta forma, se busca mejorar la eficiencia y seguridad en diversas situaciones cotidianas en las que resulta importante conocer la ubicación de las personas en todo momento.

## Características Principales 🤔

- **Registro e Inicio de Sesión de Usuarios**:
  - El registro permite a los usuarios crear cuentas con información como nombre, correo electrónico, número de teléfono y contraseña.
  - Inicio de sesión seguro con credenciales de correo electrónico y contraseña.
  - Mayor seguridad con el reconocimiento de huella dactilar, activado después de un inicio de sesión exitoso con correo electrónico y contraseña.

- **Interfaz de Usuario (UI)**:
  - La aplicación presenta una interfaz de usuario amigable con un **Bottom Navigation View** (Vista de Navegación Inferior) y un **Drawer Navigation View** (Vista de Navegación Lateral) para una navegación sencilla.

- **Pestañas de Navegación**:
  - **Inicio**: Muestra la ubicación del dispositivo y permite una cuadrícula editable para ajustar el área de interés.
  - **Usuario**: Permite a los usuarios modificar su información personal, incluyendo nombre, correo electrónico, número de teléfono y contraseña.
  - **Notificar**: Permite a los usuarios notificar a un contacto de emergencia designado cuando planean salir. Esta notificación se realiza automáticamente a través de correo electrónico.
  - **Contacto**: Facilita la gestión y edición de la información de contacto de emergencia, incluyendo nombre, dirección, correo electrónico y número de teléfono.

- **Navegación Lateral**:
  - **Acerca de**: Proporciona información sobre la versión actual de la aplicación.
  - **Cerrar Sesión**: Permite a los usuarios cerrar sesión en sus cuentas.


## Capturas de Pantalla 📷

<div style="overflow: hidden; width: 100%;">
  <div style="display: flex; overflow-x: auto; scroll-snap-type: x mandatory;">
    <img src="https://github.com/Nestor-DS/FindApp/assets/78669277/09179485-a036-4800-983f-cf87a09d7d84" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
    <img src="https://github.com/Nestor-DS/FindApp/assets/78669277/5ad4b475-4e72-435d-b6b0-84d95d2ec379" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
    <img src="https://github.com/Nestor-DS/FindApp/assets/78669277/851b48d4-b549-48c5-8c9c-ed4db43e6ad9" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
    <img src="https://github.com/Nestor-DS/FindApp/assets/78669277/c99569cf-6c50-43a4-af00-4fb5ff35836a" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
    <img src="https://github.com/Nestor-DS/FindApp/assets/78669277/150723c8-c83a-4699-88a6-1460c9c14cb6" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
    <img src="https://github.com/Nestor-DS/FindApp/assets/78669277/84c895d9-c3a6-46e1-9f7b-b5ed2525fdba" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
    <img src="https://github.com/Nestor-DS/FindApp/assets/78669277/de2f43bd-c8e7-437c-aa86-f2cd50da58bd" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
    <img src="" style="flex: 0 0 auto; width: 20%; scroll-snap-align: start;">
  </div>
</div>

## Video de Demostración 📽️

[![Alt text](https://img.youtube.com/vi/W5morje1Fjs/0.jpg)](https://www.youtube.com/watch?v=W5morje1Fjs)

Puedes ver una demostración completa de la aplicación en [este enlace](https://www.youtube.com/watch?v=1oDGYXddozE). ¡No dudes en echarle un vistazo!




## Instalación y Configuración ⚙️

Para utilizar FindApp, sigue estos pasos de instalación y configuración:

1. Asegúrate de tener los servicios necesarios activados junto con la base de datos en funcionamiento.

2. En los archivos del programa, actualiza la dirección IP del servidor donde se aloja el servicio para que coincida con tu entorno de servidor.

3. En el archivo "google_map_api.xml", asegúrate de haber configurado correctamente tu clave de API de Google Maps.

4. Ejecuta la aplicación en un dispositivo Android compatible.


## Uso 👀⚒️

FindApp ofrece una experiencia completa para mejorar la seguridad y la comodidad del usuario:

### Registro y Autenticación
- **Registro de Usuario**: Utiliza el signup para registrar al usuario con información vital, como nombre, correo electrónico, teléfono y contraseña.

- **Inicio de Sesión Seguro**: Accede a tu cuenta utilizando el correo y la contraseña. Para una capa adicional de seguridad, hemos implementado el reconocimiento de huella dactilar, que se activa automáticamente después de iniciar sesión con éxito.

### Navegación Intuitiva
- **Interfaz de Usuario Rica**: Al iniciar la aplicación, te darás cuenta de una interfaz amigable que presenta tanto un Bottom Navigation View como un Drawer Navigation View para una navegación sencilla.

- **Explora las Pestañas**: El Bottom Navigation View te brinda acceso rápido a cuatro pestañas esenciales: "Inicio", "Usuario", "Notificar" y "Contacto".

### Funcionalidades Destacadas
#### Pestaña "Inicio"
- **Ubicación en Tiempo Real**: Visualiza la ubicación en tiempo real de tu dispositivo.
- **Edición de Malla**: Modifica fácilmente la circunferencia de la malla utilizando un menú accesible que se despliega al tocar un botón flotante.

#### Pestaña "Usuario"
- **Personaliza tu Información**: Edita tu información personal, incluyendo nombre, correo electrónico, teléfono y contraseña.

#### Pestaña "Notificar"
- **Notificación de Salida**: Planifica tus salidas y notifica a un contacto de emergencia designado. El servicio automatizado envía un correo electrónico en tu nombre.

#### Pestaña "Contacto"
- **Gestión de Contactos de Emergencia**: Administra y actualiza la información de tus contactos de emergencia, incluyendo nombre, dirección, correo electrónico y teléfono.

### Explora el Drawer Navigation View
- **Navegación Completa**: El Drawer Navigation View te permite acceder a las secciones de "Inicio", "Usuario", "Contacto", "Acerca de" y cerrar sesión.

#### Sección "Acerca de"
- **Detalles de la Versión**: Encuentra información relevante sobre la versión actual de la aplicación.

Utiliza FindApp para una experiencia segura y cómoda en tu día a día.





## Contribución 🤝

Si deseas contribuir a este proyecto, sigue los siguientes pasos:

1. Realiza un fork del repositorio.
2. Crea una rama para tu contribución: `git checkout -b mi-contribucion`.
3. Realiza tus cambios y commitea: `git commit -m 'Añadir nueva funcionalidad'`.
4. Haz un push de tus cambios a tu repositorio fork: `git push origin mi-contribucion`.
5. Abre una solicitud de extracción (Pull Request) a este repositorio.

## Problema Conocido‼️ 💥

Existen problemas conocidos:

### Error de Validación de Datos
- Algunos campos de entrada no cuentan con validación de datos completa, lo que podría permitir el registro de información incorrecta o inapropiada. Se esta mejorando la validación en todos los campos para garantizar la integridad de los datos.

### Problema de Seguridad


### Problema de Rendimiento
- La aplicación puede experimentar problemas de rendimiento en algunos dispositivos o situaciones.

### Error de Interfaz de Usuario
- La interfaz de usuario puede no ser coherente en todos los dispositivos y resoluciones de pantalla. Estoy trabajando en mejorar la adaptabilidad y la usabilidad de la interfaz para ofrecer una experiencia uniforme en diversos entornos.

## Licencia 📜

Este proyecto está licenciado bajo la [nombre de la licencia]. Consulta el archivo [LICENSE](LICENSE) para obtener más detalles.

## Contacto 📫

Si sugerencias, no dudes en ponerte en contacto con [tu nombre] a través de [tu dirección de correo electrónico].
