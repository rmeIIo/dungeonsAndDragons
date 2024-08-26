package org.example.com.rogerCorrea.dnd
import com.rogerCorrea.dnd.model.Raca
import com.rogerCorrea.dnd.model.Personagem
import com.rogerCorrea.dnd.utils.Leitor
import com.rogerCorrea.dnd.model.Classe
import com.rogerCorrea.dnd.services.CalculadoraPontosDeVida
import com.rogerCorrea.dnd.services.GuerreiroPontosDeVida
import com.rogerCorrea.dnd.services.LadinoPontosDeVida
import com.rogerCorrea.dnd.services.MagoPontosDeVida
import com.rogerCorrea.dnd.services.ClerigoPontosDeVida

fun main() {
    println("DUNGEONS AND DRAGONS")
    println("Sua aventura começa logo!")
    println("Você ainda não possui nenhum personagem associado à sua conta")
    println("Criar novo personagem? (S/N)")
    val criarPersonagem = readLine()

    if(criarPersonagem.equals("S", ignoreCase = true)) {
        criarNovoPersonagem()
    } else {
        println("Até a próxima!")
    }
}

fun criarNovoPersonagem() {
    val nome = Leitor.lerString("Digite o nome do personagem: ")

    val atributos = distribuirPontosAtributos()
    val força = atributos[0]
    val destreza = atributos[1]
    val constituicao = atributos[2]
    val inteligencia = atributos[3]
    val sabedoria = atributos[4]
    val carisma = atributos[5]

    val raca = Leitor.lerRaca()
    val classeString = Leitor.lerClasse()
    val classe = Classe.valueOf(classeString.uppercase())

    val calculadoraPontosDeVida = when (classe) {
        Classe.GUERREIRO -> GuerreiroPontosDeVida()
        Classe.LADINO -> LadinoPontosDeVida()
        Classe.MAGO -> MagoPontosDeVida()
        Classe.CLERIGO -> ClerigoPontosDeVida()
    }

    val personagem = Personagem(
        nome = nome,
        forca = força,
        destreza = destreza,
        constituicao = constituicao,
        inteligencia = inteligencia,
        sabedoria = sabedoria,
        carisma = carisma,
        pontosDeVida = calculadoraPontosDeVida.calculaPontosDeVida(constituicao),
        deslocamento = 0,
        idiomas = emptyList(),
        classe = classe,
        bonusProficiencia = 2,
        calculadoraPontosDeVida = calculadoraPontosDeVida
    )

    val racaEnum = Raca.valueOf(raca.uppercase())
    personagem.aplicarBonusRacial(racaEnum)

    println("\nInformações do Personagem Criado:")
    personagem.exibirInfo()
}

fun distribuirPontosAtributos(): List<Int> {
    var pontosRestantes = 27
    val atributos = mutableListOf(8, 8, 8, 8, 8, 8)
    val nomesAtributos = listOf("Força", "Destreza", "Constituição", "Inteligência", "Sabedoria", "Carisma")

    while (pontosRestantes > 0) {
        println("\nPontos restantes: $pontosRestantes")
        println("Distribuição atual dos atributos:")
        for (i in atributos.indices) {
            println("${nomesAtributos[i]}: ${atributos[i]}")
        }

        val atributoSelecionado = Leitor.lerInt("Selecione o atributo que deseja melhorar (1-Força, 2-Destreza, 3-Constituição, 4-Inteligência, 5-Sabedoria, 6-Carisma): ")

        if (atributoSelecionado in 1..6) {
            val index = atributoSelecionado - 1
            println("${nomesAtributos[index]}: ${atributos[index]} (atual)")

            val incremento = Leitor.lerInt("Quantos pontos deseja adicionar a ${nomesAtributos[index]}? ")

            if (incremento <= pontosRestantes && (atributos[index] + incremento) <= 15) {
                atributos[index] += incremento
                pontosRestantes -= incremento
                println("${nomesAtributos[index]} agora tem ${atributos[index]} pontos.")
            } else {
                println("Valor inválido. Certifique-se de que os pontos não ultrapassem o limite de 15 e que você tenha pontos suficientes.")
            }
        } else {
            println("Seleção inválida. Escolha um número entre 1 e 6.")
        }
    }

    println("\nDistribuição final dos atributos:")
    for (i in atributos.indices) {
        println("${nomesAtributos[i]}: ${atributos[i]}")
    }

    return atributos
}