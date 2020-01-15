package com.kotlin.refactoring.model

class AuthToken {
    var token: String? = null

    constructor() {}
    constructor(token: String?) {
        this.token = token
    }
}
