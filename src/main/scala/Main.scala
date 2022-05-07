package example

import java.nio.file.Paths
import java.io.File
import java.util.Scanner
import scala.collection.mutable.Map
import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer


object Main {

  def assertion(expression: Boolean, message: String): Unit = {
  if (!expression) throw new Exception(message)
  }

  def extractWords(filePath: String): List[String] = {
    assertion((filePath != ""), "File path cannot be empty.")

    val lines = try {
      scala.io.Source.fromFile("src/main/resources/" + filePath).getLines()
    } catch {
      case _: Throwable => throw new Exception("The file does not exist.")
    }
    assertion(lines.nonEmpty, "File cannot be empty.")

    val result = new ListBuffer[String]()
    var current = ""

    while (lines.hasNext) {
      current = lines.next()
      val words = current.split(" ").toList
      words.foreach(result += _.replaceAll("[^A-Za-z]+", "").toLowerCase)
    }

    result.toList
  }

  def removeStopWords(words: List[String]): List[String] = {
    // For simplification, all words with less or equal than 3 characters are considered stop words.
    assertion(words.nonEmpty, "Can't remove stop words from empty word list.")

    var result = new ListBuffer[String]()
    for(word <- words if word.length() > 3) result.append(word)
    result.toList
  }

  def mapFrequencies(words: List[String]): Map[String, Int] = {
    assertion(words.nonEmpty, "Can't map word frequency from empty word list.")

    val wordMap = Map.empty[String, Int]
    for (word <- words) {
      if (wordMap.contains(word)) {
        val total = wordMap.get(word).get + 1
        wordMap.update(word, total)
      } else {
        wordMap.update(word, 1)
      }
    }
    wordMap
  }

  def sortMap(wordMap: Map[String, Int]): ListMap[String, Int] = {
    assertion(wordMap.nonEmpty, "Can't sort an empty map.")
    ListMap(wordMap.toSeq.sortWith(_._2 > _._2):_*)
  }

  def main(args: Array[String]): Unit = {
        try{
          assertion((args.length != 0), "No filename was passed in the command line.")

          val file = (args(0))
        
          val words = extractWords(file)
          
          val noStopWords = removeStopWords(words)

          assertion(noStopWords.nonEmpty, "No words were longer than three letters.")
          assertion((noStopWords.length > 25), "File needs to have more than 25 words.")

          val freqMap = mapFrequencies(noStopWords)
          val sortedMap = sortMap(freqMap)
          println(sortedMap)
        } catch {
          case _: Throwable => throw new Exception("NANI?! The error was way too powerful.")
        }
   }
}