## Néhány dolog a osztálydiagramokról
- Minden asszociációnak legyen neve, kivéve nagyon triviális esetekben
- Néha kell többszörös asszociációt használni, olyan esetekben amikor mondjuk két megkülönböztetett vége van egy sávnak (hiszen egyirányú)
- A modellnek minden értelmes funkcióját el kell érni interfészeken keresztül
- Mivel a kontroller majd az interfészeket látja csak, a modelltől osztályt direktbe nem kapunk, csakvalami interfészt megvalósító osztályt.
- Ami megvan asszociációként, az ne legyen meg attribútumként (inkább legyen neve az asszociációnak)
