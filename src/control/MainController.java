package control;

import model.Edge;
import model.Graph;
import model.List;
import model.Vertex;

/**
 * Created by Jean-Pierre on 12.01.2017.
 */
public class MainController {

    //Attribute

    //Referenzen
    private Graph allUsers;

    public MainController(){
        allUsers = new Graph();
        createSomeUsers();
    }

    /**
     * Fügt Personen dem sozialen Netzwerk hinzu.
     */
    private void createSomeUsers(){
        insertUser("Ulf");
        insertUser("Silent Bob");
        insertUser("Dörte");
        insertUser("Ralle");
        befriend("Silent Bob", "Ralle");
        befriend("Dörte", "Ralle");
    }

    /**
     * Fügt einen Nutzer hinzu, falls dieser noch nicht existiert.
     * @param name
     * @return true, falls ein neuer Nutzer hinzugefügt wurde, sonst false.
     */
    public boolean insertUser(String name){
        //TODO 05: Nutzer dem sozialen Netzwerk hinzufügen.
        if(allUsers.getVertex(name) == null){
            allUsers.addVertex(new Vertex(name));
            return true;
        }
        return false;
    }

    /**
     * Löscht einen Nutzer, falls dieser existiert. Alle Verbindungen zu anderen Nutzern werden ebenfalls gelöscht.
     * @param name
     * @return true, falls ein Nutzer gelöscht wurde, sonst false.
     */
    public boolean deleteUser(String name){
        //TODO 07: Nutzer aus dem sozialen Netzwerk entfernen.
        if(allUsers.getVertex(name) != null){
            allUsers.removeVertex(allUsers.getVertex(name));
            return true;
        }
        return false;
    }

    /**
     * Falls Nutzer vorhanden sind, so werden ihre Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @return
     */
    public String[] getAllUsers(){
        //TODO 06: String-Array mit allen Nutzernamen erstellen.
        if(allUsers.isEmpty() || allUsers == null) return  null;

        int count = 0;
        List<Vertex> vertices = allUsers.getVertices();
        allUsers.getVertices().toFirst();
        while (vertices.hasAccess()){
            count++;
            vertices.next();
        }


        String[] output = new String[count];
        vertices.toFirst();
        for (int i = 0; i< output.length; i++){
            output[i] = vertices.getContent().getID();
            vertices.next();
        }
        return output;
    }

    /**
     * Falls der Nutzer vorhanden ist und Freunde hat, so werden deren Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @param name
     * @return
     */
    public String[] getAllFriendsFromUser(String name){
        //TODO 09: Freundesliste eines Nutzers als String-Array erstellen.
        Vertex v = allUsers.getVertex(name);
        if(v == null) return null;

        List<Vertex> neighbours = allUsers.getNeighbours(v);
        int counter = 0;
        if(!neighbours.isEmpty()){
            neighbours.toFirst();
            while (neighbours.hasAccess()){
                counter++;
                neighbours.next();
            }

            String[] array = new String[counter];
            neighbours.toFirst();
            for(int i = 0; i < counter; i++){
                array[i] = neighbours.getContent().getID();
                neighbours.next();
            }
            return array;
        }
        return null;
    }

    /**
     * Bestimmt den Zentralitätsgrad einer Person im sozialen Netzwerk, falls sie vorhanden ist. Sonst wird -1.0 zurückgegeben.
     * Der Zentralitätsgrad ist der Quotient aus der Anzahl der Freunde der Person und der um die Person selbst verminderten Anzahl an Nutzern im Netzwerk.
     * Gibt also den Prozentwert an Personen im sozialen Netzwerk an, mit der die Person befreundet ist.
     * @param name
     * @return
     */
    public double centralityDegreeOfUser(String name){
        //TODO 10: Prozentsatz der vorhandenen Freundschaften eines Nutzers von allen möglichen Freundschaften des Nutzers.
        Vertex v = allUsers.getVertex(name);
        if(v == null) return -1.0;

        double friends = 0.0;
        double allOtherUsers = -1.0;

        List<Vertex> hFriends = allUsers.getNeighbours(v);
        hFriends.toFirst();
        while (hFriends.hasAccess()){
            hFriends.next();
            friends++;
        }

        List<Vertex> allU = allUsers.getVertices();
        allU.toFirst();
        while (allU.hasAccess()){
            allU.next();
            allOtherUsers++;
        }

        if(allOtherUsers == 0.0) return -1.0;

        return friends/allOtherUsers;
    }

    /**
     * Zwei Nutzer des Netzwerkes gehen eine Freundschaft neu ein, falls sie sich im Netzwerk befinden und noch keine Freunde sind.
     * @param name01
     * @param name02
     * @return true, falls eine neue Freundeschaft entstanden ist, ansonsten false.
     */
    public boolean befriend(String name01, String name02){
        //TODO 08: Freundschaften schließen.
        Vertex v1 = allUsers.getVertex(name01);
        Vertex v2 = allUsers.getVertex(name02);

        if(v1 != null && v2 != null){
            List<Vertex> b = allUsers.getNeighbours(v1);
            b.toFirst();
            while (b.hasAccess()){
                if(b.getContent() == v2) return false;
                b.next();
            }
            //Knoten sind drin, aber nicht befreundet Änder das!
            allUsers.addEdge(new Edge(v1 , v2 , 1));
            return true;
        }
        return false;
    }

    /**
     * Zwei Nutzer beenden ihre Freundschaft, falls sie sich im Netzwerk befinden und sie befreundet sind.
     * @param name01
     * @param name02
     * @return true, falls ihre Freundschaft beendet wurde, ansonsten false.
     */
    public boolean unfriend(String name01, String name02){
        //TODO 11: Freundschaften beenden.
        Vertex v1 = allUsers.getVertex(name01);
        Vertex v2 = allUsers.getVertex(name02);

        if(v1 != null && v2 != null && v1 != v2 && allUsers.getEdge(v1, v2) != null){
            allUsers.removeEdge(allUsers.getEdge(v1,v2));
            return true;
        }
        return false;
    }

    /**
     * Bestimmt die Dichte des sozialen Netzwerks und gibt diese zurück.
     * Die Dichte ist der Quotient aus der Anzahl aller vorhandenen Freundschaftsbeziehungen und der Anzahl der maximal möglichen Freundschaftsbeziehungen.
     * @return
     */
    public double dense(){
        //TODO 12: Dichte berechnen.
        double dense = 0.0;

        double amountOfEdges = 0.0;
        List<Edge> edgeList = allUsers.getEdges();
        edgeList.toFirst();
        while ((edgeList.hasAccess())){
            edgeList.next();
            amountOfEdges++;
        }

        double amountAllUsers = 0.0;
        List<Vertex> allU = allUsers.getVertices();
        allU.toFirst();
        while (allU.hasAccess()){
            allU.next();
            amountAllUsers++;
        }

        if(amountAllUsers == 0.0) return  -1.0;
        return amountOfEdges/moechteGernFac(amountAllUsers-1);
    }

    private double moechteGernFac(double n){
        if(n <= 0) return 0;
        return  n + moechteGernFac(n-1);

    }

    /**
     * Gibt die möglichen Verbindungen zwischen zwei Personen im sozialen Netzwerk als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return
     */
    public String[] getLinksBetween(String name01, String name02){
        //TODO 13: Schreibe einen Algorithmus, der mindestens eine Verbindung von einem Nutzer über Zwischennutzer zu einem anderem Nutzer bestimmt. Happy Kopfzerbrechen!

        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            List<Vertex> p = new List<>();
            p.append(user01);
            user01.setMark(true);

            while (!p.isEmpty() && !user02.isMarked()){
                p.toLast();
                List<Vertex> neighbours = allUsers.getNeighbours(p.getContent());
                neighbours.toFirst();
                if(!neighbours.isEmpty()){
                    while (neighbours.hasAccess() && neighbours.getContent().isMarked()){
                        neighbours.next();
                    }
                }
                if(neighbours.hasAccess()){
                    p.append(neighbours.getContent());
                    neighbours.getContent().setMark(true);
                }else{
                    p.toLast();
                    p.remove();
                }
            }
            allUsers.setAllVertexMarks(false);

            if(!p.isEmpty()) {
                int counter = 0;
                while (p.hasAccess()) {
                    p.next();
                    counter++;
                }
                String[] output = new String[counter];
                p.toFirst();
                for (int i = 0; i < output.length; i++) {
                    output[i] = p.getContent().getID();
                    p.next();
                }
                return output;
            }
        }
        return null;
    }

    /**
     * Gibt zurück, ob es einen Knoten ohne Nachbarn gibt.
     * @return true, falls ein einersamer Knoten vorhanden ist, sonst false.
     */
    public boolean someoneIsLonely(){
        //TODO 14: Schreibe einen Algorithmus, der explizit den von uns benutzten Aufbau der Datenstruktur Graph und ihre angebotenen Methoden so ausnutzt, dass schnell (!) iterativ geprüft werden kann, ob der Graph allUsers keine einsamen Knoten hat. Dies lässt sich mit einer einzigen Schleife prüfen.
        List<Vertex> list = allUsers.getVertices();
        if(!list.isEmpty()){
            while(list.hasAccess()){
                if(allUsers.getNeighbours(list.getContent()).isEmpty()) return true;
                list.next();
            }
        }
        return false;
    }

    /**
     * Gibt zurück, ob vom ersten Knoten in der Liste aller Knoten ausgehend alle anderen Knoten erreicht also markiert werden können.
     * Nach der Prüfung werden noch vor der Rückgabe alle Knoten demarkiert.
     * @return true, falls alle Knoten vom ersten ausgehend markiert wurden, sonst false.
     */
    public boolean testIfConnected(){
        //TODO 15: Schreibe einen Algorithmus, der ausgehend vom ersten Knoten in der Liste aller Knoten versucht, alle anderen Knoten über Kanten zu erreichen und zu markieren.
        if(someoneIsLonely() == true) return false;
        List<Vertex> list = allUsers.getVertices();
        if(!list.isEmpty()){
            Vertex t = list.getContent();
            list.next();
            while(list.hasAccess()){
                if(getLinksBetween(t.getID(), list.getContent().getID() == null) return false;
            }
        }
        return false;
    }
    
    /**
     * Gibt einen String-Array zu allen Knoten zurück, die von einem Knoten ausgehend erreichbar sind, falls die Person vorhanden ist.
     * Im Anschluss werden vor der Rückgabe alle Knoten demarkiert.
     * @return Alle erreichbaren Knoten als String-Array, sonst null.
     */
    public String[] transitiveFriendships(String name){
        //TODO 16: Schreibe einen Algorithmus, der ausgehend von einem Knoten alle erreichbaren ausgibt.
        Vertex user01 = allUsers.getVertex(name);

        if(user01 != null){
            List<Vertex> list = new List<>();
            list.append(user01);
            user01.setMark(true);
            list.toFirst();
            while (list.hasAccess()){
                List<Vertex> neighbours = allUsers.getNeighbours(list.getContent());
                neighbours.toFirst();
                if(!neighbours.isEmpty()){
                    while (neighbours.hasAccess() && neighbours.getContent().isMarked()){
                        neighbours.next();
                    }
                }
                if(neighbours.hasAccess()){
                    list.append(neighbours.getContent());
                    neighbours.getContent().setMark(true);
                }else{
                    list.next();
                }
                list.next();
            }
            allUsers.setAllVertexMarks(false);

            if(!list.isEmpty()) {
                int counter = 0;
                while (list.hasAccess()) {
                    list.next();
                    counter++;
                }
                String[] output = new String[counter];
                list.toFirst();
                for (int i = 0; i < output.length; i++) {
                    output[i] = list.getContent().getID();
                    list.next();
                }
                return output;
            }
        }
        return null;
    }
    
    
    /**
     * Gibt eine kürzeste Verbindung zwischen zwei Personen des Sozialen Netzwerkes als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return Verbindung als String-Array oder null, falls es keine Verbindung gibt.
    */
    public String[] shortestPath(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 17: Schreibe einen Algorithmus, der die kürzeste Verbindung zwischen den Nutzern name01 und name02 als String-Array zurückgibt. Versuche dabei einen möglichst effizienten Algorithmus zu schreiben.
        }
        return null;
    }

}
