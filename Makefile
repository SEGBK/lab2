# Makefile - lab2
# Contains all build information to make life easier.

GAME = Game
SRC  = lib/*.java $(GAME).java
DOCS = docs
TEST = test/Runner

all:
	javac $(GAME).java

run: all
	java $(GAME)

$(DOCS): .PHONY
	javadoc $(SRC) -d $(DOCS)

test: .PHONY
	javac $(TST).java
	java $(TST)

clean:
	rm *.class lib/*.class test/*.class
	rm -rf $(DOCS)

.PHONY:

