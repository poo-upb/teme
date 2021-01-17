package input;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import simulate.Change;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class InputParser {

    private Players<Consumer> consumers;
    private Players<Distributor> distributors;
    private Players<Producer> producers;
    private int numberOfTurns;
    private static InputParser instance = null;
    private final ObjectMapper objectMapper;
    private final ObjectListFactory objectListFactory;
    private JsonNode jsonNode;

    private InputParser() {
        objectMapper = new ObjectMapper();
        objectListFactory = ObjectListFactory.getInstance();
    }

    /**
     * Returns the instance
     * @return
     */
    public static InputParser getInstance() {
        if (instance == null) {
            instance = new InputParser();
        }
        return instance;
    }

    /**
     * Gets the data from the input file
     * @throws IOException
     */
    public void parse() throws IOException {

        numberOfTurns = jsonNode.get("numberOfTurns").asInt();
        JsonNode currentNode = jsonNode.get("initialData");
        consumers.addAll(objectListFactory.getObjectList(currentNode.get("consumers"),
                objectMapper, Consumer.class));
        distributors.addAll(objectListFactory.getObjectList(currentNode.get("distributors"),
                objectMapper, Distributor.class));
        producers.addAll(objectListFactory.getObjectList(currentNode.get("producers"), objectMapper,
                Producer.class));

    }

    public Players<Consumer> getConsumers() {
        return consumers;
    }

    public Players<Distributor> getDistributors() {
        return distributors;
    }
    public Players<Producer> getProducers() {
        return producers;
    }

    /**
     * Gets the new updates
     * Stores the new consumers
     * Returns a list of Cost Changes
     * @param currentUpdate
     * @return
     * @throws IOException
     */

    public void addNewCustomers(final int currentUpdate) throws IOException {
        JsonNode currentNode = jsonNode.get("monthlyUpdates").get(currentUpdate);
        consumers.addAll(objectListFactory.getObjectList(currentNode.get("newConsumers"),
                objectMapper, Consumer.class));
    }

    /**
     * Returns a list of Change objects
     * from the monthly updates
     * @param currentUpdate
     * @param key
     * @return
     * @throws IOException
     */
    public List<Change> getUpdates(final int currentUpdate, String key) throws IOException {

        JsonNode currentNode = jsonNode.get("monthlyUpdates").get(currentUpdate);
        return objectListFactory.getObjectList(currentNode.get(key),
                objectMapper, Change.class);
    }


    /**
     * Initializes the containers for consumers and distributors
     * Initializes the root json node of the file
     * @param filename
     * @throws IOException
     */
    public void openFile(final String filename) throws IOException {
        consumers = new Players<>();
        distributors = new Players<>();
        producers = new Players<>();
        jsonNode = objectMapper.readTree(new File(filename));
    }
    public int getNumberOfTurns() {
        return numberOfTurns;
    }
}
