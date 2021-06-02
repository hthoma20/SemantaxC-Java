$javacc = "lib/javacc.jar"

$grammarFile = "src/com/symantax/parser/grammar/semantax.jj"
$parserDest = "src/com/symantax/parser/generated"

java -cp $javacc javacc "-OUTPUT_DIRECTORY=$parserDest" $grammarFile
