fun main() {
    val mockClient = Client()
    val response = mockClient.perform(200, "OK")
        .andExpect {
            status {
                isOk()
            }
            body {
                isNotNull()
            }
        }.andDo { response ->
            println(response)
        }.response
}


class ResponseActions(val code: Int, val body: String?) {
    val response: Response = Response(code, body)


    fun andDo(callback: (Response) -> Unit): ResponseActions {
        callback(response)
        return this
    }
    fun andExpect(callback: ResponseMatchers.() -> Unit): ResponseActions {
        callback(ResponseMatchers())
        return this
    }

    inner class ResponseMatchers() {

        fun body(callback: BodyResponseMatchers.() -> Unit) {
            callback(BodyResponseMatchers())
        }

        fun status(callback: StatusResponseMatchers.() -> Unit) {
            callback(StatusResponseMatchers())
        }
    }

    inner class BodyResponseMatchers() {
        fun isNull() {
            if (!body.isNullOrBlank())
                throw BodyResponseMatchersException()
        }
        fun isNotNull() {
            if (body.isNullOrBlank())
                throw BodyResponseMatchersException()
        }
    }

    inner class StatusResponseMatchers() {
        fun isOk() {
            if (code != 200)
                throw StatusResponseMatchersException()
        }
        fun isBadRequest() {
            if (code != 400)
                throw StatusResponseMatchersException()
        }
        fun isInternalServerError() {
            if (code != 500)
                throw StatusResponseMatchersException()
        }
    }
}


sealed class ResponseMatchersException(message:String): Exception(message)


class StatusResponseMatchersException(): ResponseMatchersException("Ошибка запроса")


class BodyResponseMatchersException(): ResponseMatchersException("Ошибка тела запроса")