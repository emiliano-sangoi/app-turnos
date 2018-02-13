# AppTurnos

Esta aplicación fue desarrollada como trabajo práctico final para el curso de Desarrollo de Aplicaciones Móviles dictado por el laboratorio Gugler, Facultad de Ciencia y Tecnología perteneciente a la Universidad Autónoma de Entre Ríos.

![Alt text](/relative/path/to/img.jpg?raw=true "Optional Title")






## Descripción
La aplicación permite solicitar turnos y darlos de baja en distintas clinicas médicas.
Se definieron ciertas reglas de negocio basicas que guiaron el diseño e implementación de una base de datos. La misma es accedida desde la aplicación móvil a través de un servicio web del tipo REST cuya interfaz puede ser consultada el siguiente sitio: [API Turnos](https://www.emiliano-sangoi.com.ar/api-turnos/).

## Características de la aplicación
* La aplicación se encuentra desarrollada para dispositivos Android 5.1 (Llollipop) en adelante.
* Se utiliza la librería [Gson](https://github.com/google/gson) para la rápida serialización y deserialización de objetos.

## Ejemplos de usuarios y contraseñas
**Usuario:** emil34 , **Contraseña:** 9178
**Usuario:** carmelo43 , **Contraseña:** 5415
**Usuario:** ritchie.marvin , **Contraseña:** 2048
**Usuario:** ciara10, **Contraseña:** 4644

## Reglas de negocio


* Para poder solicitar turnos un paciente debe poseer un usuario y contraseña.
* La gestión asociada a los usuarios y/o contraseñas queda excluida de esta versión del proyecto. La red de clínicas decidirá cuales serán los requisitos para que a un pacienete que paciente le otorgará 
* Un usuario puede tener asociado varios turnos.
* Un turno puede estar "Finalizado" si transcurrio la fecha del turno; "Cancelado" si fue dado de baja por el usuario o "Vigente" si no se encuentra en ninguno de los otros dos estados.
* Cada turno puede tener asociado a lo sumo una única obra social.
Al crear un nuevo turno el usuario puede elegir la obra social con la que efectuará la consulta. Es necesario que la clinica en donde el médico realizará la atención médica tenga convenio con la obra social elegida.
Si esto no se cumple o el paciente no posee ninguna obra social, el usuario puede elegir pagar la consulta o elegir otro médico.
* .


## Mejoras futuras
* Notificaciones de próximos turnos.
* Geolocalización. Ofrecer al usuario la posibilidad de visualizar las ubicaciones de las clínicas y como llegar a dichas clínicas desde la ubicación actual.
* En caso de existir problemas de conectividad, permitir guardar el turno internamente en estado "Pendiente" e intentar guardarlo cuando se restablezca la conexión.
* Posibilidad de enviar mensajes o llamar a la clínica.
* Funcionalidad para realizar consultas a la clínica.