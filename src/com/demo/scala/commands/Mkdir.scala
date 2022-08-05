package com.demo.scala.commands
import com.demo.scala.files.{DirEntry, Directory}
import com.demo.scala.filesystem.State

class Mkdir(name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def doMkdir(state: State, name: String): State = {

    def updateStructure(currentDir: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if(path.isEmpty)  currentDir.addEntry(newEntry)
      else {
         val oldEntry = currentDir.findEntry(path.head).asDirectory
        currentDir.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd
    // 1. All directories in the full path
    val allDirsInPath: List[String] = wd.getAllFoldersInPath()
    // 2. Create new directory entry in the wd
    val newDir = Directory.empty(wd.path, name)
    // 3. Update the whole DS to use the new root (DS IS IMMUTABLE)
    val newRoot =  updateStructure(state.root, allDirsInPath, newDir)
    // 4. Find new WD Instance from new Directory
    val newWd = newRoot.findDescendents(allDirsInPath)

    State(newRoot, newWd)
  }

  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name))
      state.setMessage(s"Entry $name already Exists!")
    else if (name.contains(Directory.SEPARATOR))
      state.setMessage(s"$name should not have SEPERATORS ${Directory.SEPARATOR}!")
    else if (checkIllegal(name))
      state.setMessage(s"E$name : Illegal Entry Name!")
    else doMkdir(state, name)
  }
}
