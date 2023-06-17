package gg.rsmod.plugins.service.restapi.auth

class Auth {
    companion object {
        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        private val auths = mutableListOf<String>("")

        private fun generate(): String = (1..64).map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }.map(charPool::get).joinToString("")

        fun build(): String {
            var auth = generate()

            while (auths.filter { b -> b == auth }.firstOrNull() != null)
                auth = generate()

            auths[auths.size -1] = auth
            return auth
        }

        fun auth(bearer: String): Boolean = auths.filter { b -> b == bearer }.firstOrNull() != null

        fun remove(bearer: String) { auths.remove(bearer) }
    }
}