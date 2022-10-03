# encoder-decoder

Para buildar mvn clean build
Para executar java -jar ./target/Encoder_Decoder-1.0.jar

Escolha o .txt para comprimir e tratar o ruido.

Escolha o .ecc para descomprimir e tratar o ruido.

Ao escolher .txt já é sugerido a função encoder
Ao escolher .ecc já é sugerido a função decoder

Ao executar o encoder gerar um arquivo .cod (arquivo comprimido) e .ecc(arquivo comprimido que foi tratado o ruido) 
e o sistema já lê o .ecc e sugere o decoder.

Assim como ao executar o decoder gerar um arquivo decoded e o sistema já lê ele.

Se na clase Handler, a variavel global "debug" for true, irá gerar arquivos debug, versão comprimida 
e versão com tratamento somente com o binário.

