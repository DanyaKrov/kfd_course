fun main() {
    val mockClient = Client()
    val response:Response = mockClient.perform(200, "OK").let {
        it.andExpect({it.responseMatchers.status { it.statusResponseMatchers.isOk() }},
            {it.responseMatchers.body { it.bodyResponseMatchers.isNotNull() }})
        it.andDo { println(it.response)}
        it.response
    }
}


class ResponseActions(val code: Int, val body: String?) {
    val response: Response = Response(code, body)
    val responseMatchers: ResponseMatchers = ResponseMatchers()
    val bodyResponseMatchers: BodyResponseMatchers = BodyResponseMatchers()
    val statusResponseMatchers: StatusResponseMatchers = StatusResponseMatchers()

    fun andDo(callback: () -> Unit) {
        TODO()
    }
    fun andExpect(callback1: () -> Unit, callback2: () -> Unit) {
        TODO()
    }

    class ResponseMatchers() {
        fun status(callback: () -> Unit) {
            TODO()
        }
        fun body(callback: () -> Unit) {
            TODO()
        }
    }

    class BodyResponseMatchers() {
        fun isNull() {}
        fun isNotNull() {}
    }

    class StatusResponseMatchers() {
        fun isOk() {} // если статус не 200, то выбросить исключение
        fun isBadRequest() {} // если статус не 400, то выбросить исключение
        fun isInternalServerError() {} // если статус не 500, то выбросить исключение
    }
}