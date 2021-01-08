package com.example.demo.permission

class PermissionResult(var success : Boolean = true, var failPermission : Array<out String>? = null) {

    override fun toString(): String {
        return "PermissionResult(success=$success, failPermission=${failPermission?.contentToString()})"
    }
}
