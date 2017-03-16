# xNote

I was spending too much time formatting documents so I spent more time making this instead.

Uses Apache POI to generate XML docx files.

Compile & Run, Unix:
```
javac -cp lib/*:. *.java && java -cp lib/*:. Driver sample.xnote sample.docx
```

Compile & Run, Windows:
```
javac -cp lib/*;. *.java && java -cp lib/*;. Driver sample.xnote sample.docx
```

Run, with JAR File:
```
java -jar xNote.jar sample.xnote sample.docx
```