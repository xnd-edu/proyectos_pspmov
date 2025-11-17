package com.example.navigation.data.common

@Suppress("unused")
object Constants {
    // Database
    const val DATABASE_NAME = "app_database"
    const val DATABASE_ASSET_PATH = "database/app_database.db"

    // Table Names
    const val TABLE_COCHES = "coches"
    const val TABLE_CONDUCTORES = "conductores"
    const val TABLE_COCHE_CONDUCTOR_CROSS_REF = "coche_conductor_cross_ref"

    // Column Names - Coches
    const val COLUMN_MATRICULA = "matricula"
    const val COLUMN_MARCA = "marca"
    const val COLUMN_MODELO = "modelo"
    const val COLUMN_ELECTRICO = "electrico"
    const val COLUMN_FECHA_MATRICULACION = "fechaMatriculacion"
    const val COLUMN_COLOR = "color"
    const val COLUMN_TIPO = "tipo"
    const val COLUMN_COMENTARIOS = "comentarios"

    // Column Names - Conductores
    const val COLUMN_DNI = "dni"
    const val COLUMN_NOMBRE = "nombre"
    const val COLUMN_APELLIDOS = "apellidos"
    const val COLUMN_TELEFONO = "telefono"
    const val COLUMN_FECHA_NACIMIENTO = "fechaNacimiento"
    const val COLUMN_GENERO = "genero"

    // Column Names - Cross Reference
    const val COLUMN_COCHE_MATRICULA = "cocheMatricula"
    const val COLUMN_CONDUCTOR_DNI = "conductorDni"
}