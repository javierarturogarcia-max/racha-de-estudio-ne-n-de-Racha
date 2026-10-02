package com.example.data

data class MotivationalQuote(
    val quote: String,
    val author: String
)

object MotivationalQuotes {
    val quotes = listOf(
        MotivationalQuote(
            quote = "La disciplina es el puente entre tus metas y tus mayores logros.",
            author = "Jim Rohn"
        ),
        MotivationalQuote(
            quote = "El éxito es la suma de pequeños esfuerzos repetidos día tras día.",
            author = "Robert Collier"
        ),
        MotivationalQuote(
            quote = "No cuentes los días: haz que cada día cuente.",
            author = "Muhammad Ali"
        ),
        MotivationalQuote(
            quote = "La constancia supera al talento cuando el talento no se esfuerza.",
            author = "Tim Notke"
        ),
        MotivationalQuote(
            quote = "El fuego de tu racha se alimenta de tu decisión, no de tus ganas.",
            author = "Filosofía Racha"
        ),
        MotivationalQuote(
            quote = "Cada página que estudias hoy es un escalón hacia tu libertad mañana.",
            author = "Mentalidad de Crecimiento"
        ),
        MotivationalQuote(
            quote = "Estudiar duele un rato; la ignorancia y el arrepentimiento duelen toda la vida.",
            author = "Proverbio Estudiantil"
        ),
        MotivationalQuote(
            quote = "Tu 'yo' del futuro te agradecerá infinitamente lo que hiciste hoy.",
            author = "Visión de Futuro"
        ),
        MotivationalQuote(
            quote = "Un 1% mejor cada día equivale a ser 37 veces mejor al final del año.",
            author = "James Clear"
        ),
        MotivationalQuote(
            quote = "La motivación te ayuda a empezar. El hábito te mantiene avanzando.",
            author = "Jim Ryun"
        ),
        MotivationalQuote(
            quote = "No tienes que ser grande para empezar, pero tienes que empezar para ser grande.",
            author = "Zig Ziglar"
        ),
        MotivationalQuote(
            quote = "La mente que se abre a un nuevo conocimiento jamás vuelve a su tamaño original.",
            author = "Albert Einstein"
        ),
        MotivationalQuote(
            quote = "El dolor del esfuerzo es temporal, pero el orgullo de dominar un tema es para siempre.",
            author = "Enfoque Total"
        ),
        MotivationalQuote(
            quote = "Siembra en el silencio de tu estudio y cosecharás en el estruendo de tu éxito.",
            author = "Disciplina Implacable"
        ),
        MotivationalQuote(
            quote = "Las batallas se ganan mucho antes del examen, en la rutina de cada noche.",
            author = "Sun Tzu Moderno"
        ),
        MotivationalQuote(
            quote = "Haz hoy lo que otros no quieren, y mañana podrás vivir como otros no pueden.",
            author = "Jerry Rice"
        ),
        MotivationalQuote(
            quote = "El genio es 1% de inspiración y 99% de transpiración y estudio.",
            author = "Thomas Edison"
        ),
        MotivationalQuote(
            quote = "La pereza viaja tan despacio que la pobreza no tarda en alcanzarla. ¡Tú elegiste avanzar!",
            author = "Benjamin Franklin"
        ),
        MotivationalQuote(
            quote = "No te detengas cuando estés cansado; detente cuando hayas terminado tu meta.",
            author = "Marilyn Monroe"
        ),
        MotivationalQuote(
            quote = "El conocimiento es el único tesoro que los ladrones no pueden robarte jamás.",
            author = "Séneca"
        ),
        MotivationalQuote(
            quote = "Un guerrero del estudio no espera condiciones perfectas: las crea con su voluntad.",
            author = "Código de Enfoque"
        ),
        MotivationalQuote(
            quote = "La diferencia entre un sueño y una meta es la disciplina de estudiar todos los días.",
            author = "Hábitos de Oro"
        ),
        MotivationalQuote(
            quote = "Nadie que haya dado su máximo esfuerzo en el estudio se ha arrepentido jamás.",
            author = "George Halas"
        ),
        MotivationalQuote(
            quote = "El verdadero poder no es lo que sabes hoy, sino tu capacidad de aprender cada día.",
            author = "Carol Dweck"
        ),
        MotivationalQuote(
            quote = "Hoy encendiste la chispa. Mantén la llama viva mañana.",
            author = "Espíritu de Racha"
        ),
        MotivationalQuote(
            quote = "La excelencia no es un acto aislado, es un hábito diario.",
            author = "Aristóteles"
        ),
        MotivationalQuote(
            quote = "Quien domina su atención, domina su destino y su carrera.",
            author = "Deep Work"
        ),
        MotivationalQuote(
            quote = "La inversión en conocimiento siempre paga los mejores intereses.",
            author = "Benjamin Franklin"
        ),
        MotivationalQuote(
            quote = "Convierte cada hora de estudio en tu obra de arte personal.",
            author = "Maestría Continua"
        ),
        MotivationalQuote(
            quote = "El secreto para salir adelante simplemente consiste en empezar.",
            author = "Mark Twain"
        )
    )

    fun getRandomQuote(currentIndex: Int = -1): Pair<MotivationalQuote, Int> {
        if (quotes.isEmpty()) {
            return Pair(
                MotivationalQuote(
                    "La constancia es la clave de todo gran logro.",
                    "RACHA"
                ),
                0
            )
        }
        var nextIndex = (quotes.indices).random()
        if (quotes.size > 1 && nextIndex == currentIndex) {
            nextIndex = (currentIndex + 1) % quotes.size
        }
        return Pair(quotes[nextIndex], nextIndex)
    }
}
