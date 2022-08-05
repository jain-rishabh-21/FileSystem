package com.demo.scala.commands
import com.demo.scala.files.DirEntry
import com.demo.scala.filesystem.State

class Ls extends Command {
  override def apply(state: State): State = {
    val contents = state.wd.contents
    val outputMesage = createFormattedOutput(contents)
    state.setMessage(outputMesage)
  }

  def createFormattedOutput(contents: List[DirEntry]): String =
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      s"${entry.name} [${entry.getType}]" + "\n" + createFormattedOutput(contents.tail)
    }
}
