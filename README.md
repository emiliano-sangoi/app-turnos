# AppTurnos


Esta aplicación fue desarrollada como trabajo práctico final para el curso de Desarrollo de Aplicaciones Móviles dictado por el laboratorio [Gugler](https://www.gugler.com.ar/), Facultad de Ciencia y Tecnología perteneciente a la Universidad Autónoma de Entre Ríos (UADER).

## Capturas de pantalla

<table>
   <tbody>
      <tr>
         <td><img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot1.png?raw=true" title= "Pantalla de login" /> </td>
         <td> 
            <img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot2.png?raw=true" title= "Listado de turnos y menú" /> 
         </td>
         <td> 
            <img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot3.png?raw=true" title= "Selección de una especialidad médica." /> 
         </td>
         <td> 
            <img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot4.png?raw=true" title= "Selección de un día" /> 
         </td>
      </tr>
      <tr>
         <td><img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot4b.png?raw=true" title= "Selección de horario" /> </td>
         <td> 
            <img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot5.png?raw=true" title= "Revisar datos y crear turno." /> 
         </td>
         <td> 
            <img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot6.png?raw=true" title= "Consultar información sobre un turno creado." /> 
         </td>
         <td> 
            <img src="https://github.com/emiliano-sangoi/app-turnos/blob/master/screenshoots/screenshot2c.png?raw=true" title= "Listado de turnos" /> 
         </td>
      </tr>
   </tbody>
</table>






## Descripción
La aplicación permite solicitar turnos y darlos de baja en distintas clinicas médicas.
Se definieron ciertas reglas de negocio basicas que guiaron el diseño e implementación de una base de datos. La misma es accedida desde la aplicación móvil a través de un servicio web del tipo REST cuya interfaz puede ser consultada el siguiente sitio: [API Turnos](https://www.emiliano-sangoi.com.ar/api-turnos/).

## Características de la aplicación
* La aplicación se encuentra desarrollada para dispositivos Android 5.1 (Llollipop) en adelante.
* Se utiliza la librería [Gson](https://github.com/google/gson) para la rápida serialización y deserialización de objetos.
* Se utiliza la librería [MaterialDateTimePicker](https://github.com/wdullaer/MaterialDateTimePicker) para mejorar y facilitar la selección de la fecha del turno.



## Ejemplos de usuarios y contraseñas
**Usuario:** emil34 , **Contraseña:** 9178 <br/>
**Usuario:** carmelo43 , **Contraseña:** 5415 <br/>
**Usuario:** ritchie.marvin , **Contraseña:** 2048 <br/>
**Usuario:** ciara10, **Contraseña:** 4644 <br/>

## Reglas de negocio


* Para poder solicitar turnos un paciente debe poseer un usuario y contraseña.
* La gestión asociada a los usuarios y/o contraseñas queda excluida de esta versión del proyecto. La red de clínicas decidirá cuales serán los requisitos para que a un pacienete que paciente le otorgará 
* Un usuario puede tener asociado varios turnos.
* Un turno puede estar "Finalizado" si transcurrio la fecha del turno; "Cancelado" si fue dado de baja por el usuario o "Vigente" si no se encuentra en ninguno de los otros dos estados.
* Cada turno puede tener asociado a lo sumo una única obra social.
Al crear un nuevo turno el usuario puede elegir la obra social con la que efectuará la consulta. Es necesario que la clinica en donde el médico realizará la atención médica tenga convenio con la obra social elegida.
Si esto no se cumple o el paciente no posee ninguna obra social, el usuario puede elegir pagar la consulta o elegir otro médico.
* Un paciente puede cancelar un turno cuando quiera siempre que este se encuentre vigente.
* Un medico desempeña una única especialidad en un sanatorio.


## Mejoras futuras
* Notificaciones de próximos turnos.
* Geolocalización. Ofrecer al usuario la posibilidad de visualizar las ubicaciones de las clínicas y como llegar a dichas clínicas desde la ubicación actual.
* En caso de existir problemas de conectividad, permitir guardar el turno internamente en estado "Pendiente" e intentar guardarlo cuando se restablezca la conexión.
* Posibilidad de enviar mensajes o llamar a la clínica.
* Funcionalidad para realizar consultas a la clínica.
* Pantallas y funcionalidades para cuando se loguee un médico. Cancelar o suspender turnos, consultas de turnos en el día, semana, etcetera.
* Más..

## Notas
* Los datos del servicio web fueron generados aleatoriamente. Es posible que existan algunas inconsistencias.

## Autor
[Emiliano Sangoi](https://www.linkedin.com/in/emiliano-sangoi-456b84a2/)

## Licencia
El proyecto esta bajo licencia GPL v3, ver [Sitio web GPL v3](https://www.gnu.org/licenses/gpl-3.0.en.html) para mas detalles.