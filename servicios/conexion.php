<?php
    $mysql = new mysqli("localhost", "root", "", "findapp");

    if($mysql->connect_error) {
        die("Error al conectar a la base de datos: ".$mysqli->connect_error);
    }
    else{
        echo("Felicidades, conexion exitosa");
}
