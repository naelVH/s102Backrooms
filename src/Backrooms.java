class Backrooms extends Program {

    // Pensez à réduire les différents Tabs (Coord, Case, Labyrinthe, Player, Algorithme) pour mieux vous repérer et mieux naviguer dans le code :]

    final Coord coordDeBase = newCoord(0, 8);

    final boolean T = true;
    final boolean F = false;

    final String CLEAR_SCREEN = "\033\143";

    final String[] COULEURS = new String[]{ANSI_RED, ANSI_YELLOW, ANSI_GREEN, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN};
    final int NB_COULEURS = length(COULEURS);

    void clearTerminal() {
        println(CLEAR_SCREEN);
    }

    String textToYellow(String text) {
        // transforme un String en jaune si celui-ci est compris entre 2 étoile (*)
        // attention, à utiliser que avec les fichier .txt de /display
        // aucune étoile (*) n'est visible après le passage dans cet fonction
        String newText = "";
        boolean isItIn = false;

        for (int idx = 0; idx < length(text)-1; idx ++) {

            if ( isItIn ) {

                if ( charAt(text, idx) == '*'  && charAt(text, idx+1) == '*' ) {
                    isItIn = false;
                    newText += ANSI_WHITE;
                } else if ( charAt(text, idx) == '*' ) {
                    // rien :]
                } else {
                    newText += charAt(text, idx);
                }

            } else {

                if ( charAt(text, idx) == '*'  && charAt(text, idx+1) == '*' ) {
                    isItIn = true;
                    newText += ANSI_YELLOW;
                } else if ( charAt(text, idx) == '*' ) {
                    // rien :]
                } else {
                    newText += charAt(text, idx);
                }

            }
        }

        return newText;

    }

    String fileAsString(String filename) {
        extensions.File f = new extensions.File(filename);
        String content = "";
        while (ready(f)) {
            content = content + readLine(f) + '\n';
        }
        
        return textToYellow(content);
    }

    void affichageStylé (String texte) {
        String disp = "";
        for (int idx = 0; idx < length(texte); idx++) {
            clearTerminal();
            print(substring(texte, 0, idx+1));
            delay(10);
            
        }
    }

// --------------------------------------------------------------------            Coord           -----------------------------------------------------------------------------

    Coord newCoord(int x, int y) {
        Coord c = new Coord();
        c.x = x;
        c.y = y;
        return c;
    }

    String toString(Coord c) {
        return "(" + c.x + ";" + c.y + ")";
    }

    boolean estDansLeLaby (Coord c, Labyrinthe laby) {
        return ( 0 <= c.x && c.x < laby.nbColonne && 0 <= c.y && c.y < laby.nbLigne);
    }

// --------------------------------------------------------------------            Case            -----------------------------------------------------------------------------

    Case newCase(boolean[] barrieres) {
        Case c = new Case();
        c.barrieres = barrieres;
        return c;
    }

    String toString(Case c) {
        String res = "";
        if (c.barrieres[0]) {
            res += ".___.";
        } else {
            res += ".   .";
        }

        res += '\n';

        if (c.barrieres[3]) {
            res += '|';
        } else {
            res += ' ';
        }

        res += "   ";

        if (c.barrieres[1]) {
            res += '|';
        } else {
            res += ' ';
        }

        res += '\n';

        if (c.barrieres[2]) {
            res += ".___.";
        } else {
            res += ".   .";
        }

        return res;
    }

    // Définition des 16 cases possibles selon leurs barrières
    public final Case C0 = newCase(new boolean[]{F, F, F, F});
    public final Case C1 = newCase(new boolean[]{T, F, F, F});
    public final Case C2 = newCase(new boolean[]{F, T, F, F});
    public final Case C3 = newCase(new boolean[]{T, T, F, F});
    public final Case C4 = newCase(new boolean[]{F, F, T, F});
    public final Case C5 = newCase(new boolean[]{T, F, T, F});
    public final Case C6 = newCase(new boolean[]{F, T, T, F});
    public final Case C7 = newCase(new boolean[]{T, T, T, F});
    public final Case C8 = newCase(new boolean[]{F, F, F, T});
    public final Case C9 = newCase(new boolean[]{T, F, F, T});
    public final Case C10 = newCase(new boolean[]{F, T, F, T});
    public final Case C11 = newCase(new boolean[]{T, T, F, T});
    public final Case C12 = newCase(new boolean[]{F, F, T, T});
    public final Case C13 = newCase(new boolean[]{T, F, T, T});
    public final Case C14 = newCase(new boolean[]{F, T, T, T});
    public final Case C15 = newCase(new boolean[]{T, T, T, T});

// -------------------------------------------------------------------          Labyrinthe            --------------------------------------------------------------------------

    Labyrinthe newLabyrinthe(Case[][] map) {
        Labyrinthe laby = new Labyrinthe();
        laby.map = map;
        laby.nbLigne = length(map, 1);
        laby.nbColonne = length(map, 2);
        return laby;
    }

    String toString(Labyrinthe laby) {
        String res = "";
        Case[][] matrice = laby.map;
        int nbLigne = laby.nbLigne;
        int nbColonne = laby.nbColonne;

        for (int ligne = 0; ligne < nbLigne; ligne++) {

            // ligne impair
            for (int colonne = 0; colonne < nbColonne; colonne++) {

                res += '.';
                if (matrice[ligne][colonne].barrieres[0]) {
                    res += "_______________";
                } else {
                    res += "               ";
                }

            }
            res += ".\n";

            // ligne pair
            for (int i = 0; i < 3; i++) {
                for (int colonne = 0; colonne < nbColonne; colonne++) {
                        if (matrice[ligne][colonne].barrieres[3]) {
                            res += "|";
                        } else {
                            res += ' ';
                        }
                        res += "               ";

                }

                if (matrice[ligne][nbColonne -1].barrieres[1]) {
                    res += "|";
                } else {
                    res += ' ';
                }
                res += '\n';

            }

            res += '.';
            if (matrice[nbLigne-1][nbColonne].barrieres[2]) {
                res += "_______________";
            } else {
                res += "               ";
            }

        }
        res += '.';

        return res;

    }


 // -------------------------------------------------------------------            Rooms             ---------------------------------------------------------------------

  //   ----------------------------------------------------------------              L0               ----------------------------------------------------------------------
    final Labyrinthe L001 = newLabyrinthe(new Case[][]{{C9, C5, C2},
                                                       {C12, C3, C10},
                                                       {C13, C4, C6}});
    final Labyrinthe L002 = newLabyrinthe(new Case[][]{{C0, C0, C0},
                                                       {C0, C0, C0},
                                                       {C0, C0, C0}});

  //   ----------------------------------------------------------------              L1               ----------------------------------------------------------------------
    final Labyrinthe L101 = newLabyrinthe(new Case[][]{{C13, C1, C1, C3, C9, C1, C1, C1, C3},
                                                     {C13, C3, C10, C10, C14, C10, C14, C8, C6},
                                                     {C13, C0, C6, C8, C3, C12, C3, C8, C3},
                                                     {C9, C4, C3, C14, C10, C9, C6, C12, C2},
                                                     {C8, C3, C12, C7, C8, C4, C1, C7, C10},
                                                     {C12, C0, C7, C11, C8, C7, C10, C13, C10},
                                                     {C13, C0, C3, C8, C0, C1, C4, C7, C10},
                                                     {C13, C2, C10, C8, C6, C10, C9, C5, C6},
                                                     {C13, C6, C14, C12, C5, C6, C12, C5, C5}});

  //   ----------------------------------------------------------------              L2               ----------------------------------------------------------------------
    final Labyrinthe L201 = newLabyrinthe(new Case[][]{{C9, C5, C3},
                                                     {C12, C3, C10},
                                                     {C13, C6, C10}});

  //   ----------------------------------------------------------------              L3               ----------------------------------------------------------------------
    // 1er labyrinthe crée
    final Labyrinthe L301 = newLabyrinthe(new Case[][]{{C9, C5, C3},
                                                     {C4, C3, C10},
                                                     {C13, C4, C6}});
    
    // .___.___.___.
    // |           |
    // .   .___.   .
    //         |   |
    // .___.   .   .
    // |           |
    // .___.___.___.
    

    



    
    
  // ----------------------------------------------------------------             All Rooms             -------------------------------------------------------------------

    // ici les différents sous-tableaux correspondent aux différentes portes (0: haut, 1: droite, 2: bas, 3: gauche)
    final Labyrinthe[][] allRooms = new Labyrinthe[][]{{L001},
                                                      {L101},
                                                      {L201},
                                                      {L301}};


// -------------------------------------------------------------------            Chemin              --------------------------------------------------------------------------

    Chemin newChemin(Labyrinthe[] enchainement) {
        Chemin c = new Chemin();
        c.enchainement = enchainement;
        c.nbRooms = length(enchainement);
        return c;
    }

    String toString(Chemin c) {
        return "Ce chemin contient " + c.nbRooms + "salles.";
    }

    int random(int borneMin, int borneMax) {
        return (int) (borneMin + random() * (borneMax-borneMin+1) );
    }

    void testRandom() {
        int r = random(3, 10);
        assertTrue(3 <= r && r <= 10);
    }

    Chemin genererCheminAlea(Labyrinthe[] base) {
        int taille = length(base);
        Labyrinthe[] encGenere = new Labyrinthe[taille];
        int r;

        boolean[] verif = new boolean[taille];
        for (int idx = 0; idx < taille; idx++) {
            verif[idx] = false;
        }

        for (int idx = 0; idx < taille; idx++) {
            do {
                r = random(0, taille-1);
            } while (verif[r]);

            verif[r] = true;
            encGenere[idx] = base[r];

        }

        return newChemin(encGenere); 
    }

    Chemin genererCheminAlea2(Labyrinthe[][] base) {
        int taille = 0;
        for (int idx = 0; idx < length(base); idx++) {
            for (int jdx = 0; jdx < length(base[idx]); jdx++) {
                taille++;
            }
        }
        Labyrinthe[] baseAplati = new Labyrinthe[taille];

        int cpt = 0;
        for (int idx = 0; idx < length(base); idx++) {
            for (int jdx = 0; jdx < length(base[idx]); jdx++) {
                baseAplati[cpt] = base[idx][jdx];
                cpt++;
            }
        }

        return genererCheminAlea(baseAplati);
    }


// -------------------------------------------------------------------            Player              --------------------------------------------------------------------------

    Player newPlayer(String name) {
        Player p = new Player();
        p.name = name;
        if (equals(name, "godmode") || ( equals(name, "vision") )) {
            p.vision = 999;
        } else {
            p.vision = 1;
        }
        p.toursPasses = 1;
        return p;
    }

    String toString(Player p) {
        return p.name + ' ' + toString(p.coord);
    }

    String affichagePlayerOnLaby (Labyrinthe laby, Player p) {
        String res = "";
        Case[][] matrice = laby.map;
        int nbLigne = laby.nbLigne;
        int nbColonne = laby.nbColonne;
        int nDispC = 27/nbLigne; // correspond à la taille de l'affichage relativement à la taille du laby
        int nDispL = nDispC*5;; // pareil

        int x = p.coord.x;
        int y = p.coord.y;
        int vision = p.vision;


        boolean rainbowMode = equals(p.name, "godmode") || equals(p.name, "rainbow");
        String couleur = ANSI_YELLOW;
        int cpt = 0;


        for (int ligne = 0; ligne < nbLigne; ligne++) {

            // ligne impair
            for (int colonne = 0; colonne < nbColonne; colonne++) {
                
                if ( rainbowMode ) {
                    couleur = COULEURS[cpt % NB_COULEURS];
                    cpt++;
                }

                res += '.';
                if ( ((ligne - y > vision) || (ligne - y < (-vision))) || ((colonne - x > vision) || (colonne - x < (-vision))) ) {
                    // res += couleur + "*".repeat(nDispL) + ANSI_WHITE;
                    res += couleur + "***************" + ANSI_WHITE;
                } else {

                    if (matrice[ligne][colonne].barrieres[0]) {
                        // res += couleur + "_".repeat(nDispL) + ANSI_WHITE;
                        res += couleur + "_______________" + ANSI_WHITE;
                    } else {
                        res += "               ";
                    }
                }

            }
            res += ".\n";

            // ligne pair
            for (int i = 0; i < nDispC; i++) {
                for (int colonne = 0; colonne < nbColonne; colonne++) {

                    if ( rainbowMode ) {
                        couleur = COULEURS[cpt % NB_COULEURS];
                        cpt++;
                    }

                    if ( ((ligne - y > vision) || (ligne - y < (-vision))) || ((colonne - x > vision) || (colonne - x < (-vision))) ) {
                        // res += couleur + "*".repeat(nDispL) + ANSI_WHITE;
                        res += couleur + "****************" + ANSI_WHITE;
                    } else {

                        if (matrice[ligne][colonne].barrieres[3]) {
                            res += couleur + "|" + ANSI_WHITE;
                        } else {
                            res += ' ';
                        }
                        if (i == nDispC / 2 && ligne == y && colonne == x) {
                            // res += couleur + " ".repeat(nDispL/2) + ANSI_WHITE + "P" + couleur + " ".repeat(nDispL/2) + ANSI_WHITE;
                            res += "       P       ";
                        } else {
                            // res += couleur + " ".repeat(nDispL) + ANSI_WHITE;
                            res += "               ";
                        }

                    }

                }

                if ( ((ligne - y > vision) || (ligne - y < (-vision))) || ((nbColonne -1 - x > vision) || (nbColonne -1 - x < (-vision))) ) {
                    res += couleur + "*" + ANSI_WHITE;
                } else {
                    if (matrice[ligne][nbColonne -1].barrieres[1]) {
                        res += couleur + "|" + ANSI_WHITE;
                    } else {
                        res += ' ';
                    }
                }
                res += '\n';
                

            }

        }

        // derniere ligne
        for (int colonne = 0; colonne < nbColonne; colonne++) {

            res += '.';

            if ( rainbowMode ) {
                couleur = COULEURS[cpt % NB_COULEURS];
                cpt++;
            }

            if ( ((nbLigne -1 - y > vision) || (nbLigne -1 - y < (-vision))) || ((colonne - x > vision) || (colonne - x < (-vision))) ) {
                // res += couleur + "*".repeat(nDispL) + ANSI_WHITE;
                res += couleur + "***************" + ANSI_WHITE;
            } else {
                if (matrice[nbLigne-1][colonne].barrieres[2]) {
                    // res += couleur + "_".repeat(nDispL) + ANSI_WHITE;
                    res += couleur + "_______________" + ANSI_WHITE;
                } else {
                    res += "               ";
                }
            }

        }
        res += '.';

        return res;

    }

    boolean deplacement(Player p, Labyrinthe laby, int toursMax) {
        // renvoie true si le joueur veut sortir à tout prix 
        int x = p.coord.x;
        int y = p.coord.y;
        boolean[] barrieres = laby.map[y][x].barrieres;
        String input;
        
        while (true) {
            clearTerminal();
            println(affichagePlayerOnLaby(laby, p));
            println(p.toursPasses + " / " + toursMax);
            print("Où voulez vous allez ? ");
            input = readString();
            if (equals(input, "exit"))
                break;
            if ( equals(p.name, "godmode") && ( equals(input, "z") || equals(input, "q") || equals(input, "s") || equals(input, "d") ) )
                break;
            if ( (equals(input, "z") && ! barrieres[0]) || (equals(input, "q") && ! barrieres[3]) || (equals(input, "s") && ! barrieres[2]) || (equals(input, "d") && ! barrieres[1]) ) 
                break;
            else {
                clearTerminal();
            }
        }

        p.toursPasses ++;
        if (equals(input, "z")) {
            p.coord.y -= 1;
        } else if (equals(input, "q")) {
            p.coord.x -= 1;
        } else if (equals(input, "s")) {
            p.coord.y += 1;
        } else if (equals(input, "d")) {
            p.coord.x += 1;
        }
        return equals(input, "exit");

    }

// -------------------------------------------------------------------           Affichage            --------------------------------------------------------------------------

    String menu() {
        
        String affichage = fileAsString("./ressources/display/menu.txt");
        String temp;
        while ( true ) {

            clearTerminal();
            println(affichage);

            return readString();
        
        }

    }

    void intro() {

        clearTerminal();
        String affichage = fileAsString("./ressources/display/intro.txt");
        println(affichage);
        readString();
    }

    void regles() {

        String nav = "1234567890";
        while ( equals(nav, "1234567890") ) {
            clearTerminal();
            String affichage = fileAsString("./ressources/display/regles.txt");
            println(affichage);
            nav = readString();
            if (equals(nav, "1234567890") ) {
                readme();
            }
        }
    }

    void perdu() {

        clearTerminal();
        String affichage = fileAsString("./ressources/display/perdu.txt");
        println(affichage);
        readString();
    }

    void gagne() {

        clearTerminal();
        String affichage = fileAsString("./ressources/display/gagne.txt");
        println(affichage);
        readString();
    }

    void readme() {

        clearTerminal();
        String affichage = fileAsString("./README.md");
        println(affichage);
        readString();
    }


// -------------------------------------------------------------------          Algorithme            --------------------------------------------------------------------------

    


    void algorithm() {


        intro();

        String nav = "jsp frère";
        boolean veutSortir = false;

        while ( ! equals(nav, "3")) {

            nav = menu();

            if ( equals(nav, "4") ) {
                intro();
            }

            if ( equals(nav, "2") ) {
                regles();
            }

            if ( equals(nav, "1") ) {

                Chemin cheminement = newChemin(new Labyrinthe[] {L201, L301, L001, L101});
                cheminement = genererCheminAlea2(allRooms);

                Labyrinthe labyUtil = cheminement.enchainement[0];
                int toursMax = 999;

                clearTerminal();
                // définition du nom du joueur
                affichageStylé("entrez votre nom de Joueur : ");
                String playerName = readString();
                if ( equals(playerName, "") || equals(playerName, " ") ) {
                    playerName = "joueur";
                }
                Player p = newPlayer(playerName);
                p.coord = newCoord(0, labyUtil.nbLigne -1);

                for (int currentRoom = 0; currentRoom < cheminement.nbRooms; currentRoom++) {
                    labyUtil = cheminement.enchainement[currentRoom]; 
                    p.coord = newCoord(0, labyUtil.nbLigne -1);
                    while (estDansLeLaby(p.coord, labyUtil) && p.toursPasses <= toursMax) {
                        veutSortir = deplacement(p, labyUtil, toursMax);
                        if ( veutSortir ) 
                            break;
                    }
                }

                /*
                }
                while (estDansLeLaby(p.coord, labyUtil) && p.toursPasses <= toursMax) {
                    veutSortir = deplacement(p, labyUtil, toursMax);
                    if ( veutSortir ) 
                        break;
                }
                */

                if ( ! veutSortir ) {
                    if ( estDansLeLaby(p.coord, labyUtil)) {
                        perdu();
                    } else {
                        gagne();
                    }
                }

            }
        
        }

        clearTerminal();
        reset();

    }

}