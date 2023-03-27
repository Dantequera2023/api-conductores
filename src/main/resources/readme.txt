PENDIENTES:

- Externalizar los mensajes a un fichero separado del application.properties.

- Hacer que funcione el flyway

- Mejorar el package de persistencia para que tenga control de varios datasources con vistas a que sea intercambiable
  con Spring Batch.

- Convertir el proyecto a multimodelo.

-El flyway para su correcto funcionamiento hay que dejarlo con el schema de public y luego para posteriores cambios en la BBDD como puedan ser inserciones
 hay que cambiar el archivo de V1.0__create_tables a V1.1__instert_tables e ir metiendo ahi las querys,si para un futuro se quiere añadir algo mas se tiene
 que meter un tercer archivo V1.2 con los cambios y querys a realizar
-el scope de tombcat hay que dejarlo comentado
