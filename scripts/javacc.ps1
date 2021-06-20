$javacc = "lib/javacc.jar"

$grammarFile = "src/com/semantax/parser/grammar/semantax.jj"
$parserDest = "src/com/semantax/parser/generated"

java -cp $javacc javacc "-OUTPUT_DIRECTORY=$parserDest" $grammarFile
