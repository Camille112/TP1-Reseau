Serveur de chat.


Pour lancer l'application TCP :
1. Compiler les fichiers :
Dans un terminal à l'emplacement "TP1-Reseau\Code-Socket\src" : 
$javac controller/*.java
$javac main/*.java
$javac stream/*.java
$javac streamudp/*.java
$javac view/*.java

2. Lancer le server : 
Dans un terminal à l'emplacement "TP1-Reseau\Code-Socket\src" : 
$java stream.EchoServerMultiThreaded

3. Lancer un Client : lancer le main de EchoClient


Pour lancer l'application UDP :
1. Compiler les fichiers.
2. Lancer un Client :
Dans un terminal à l'emplacement "TP1-Reseau\Code-Socket\src" : 
$java streamudp.UDP_Client
(puis écrire dans le terminal pour le chat)