import structures.AssociativeArray;
import structures.KeyNotFoundException;
import java.util.Optional;

public class AACCategory {
    private String name;
    private AssociativeArray<String, String> imageToTextMap;

    /**
     * Creates a new empty category with the given name.
     *
     * @param name the name of the category.
     */
    public AACCategory(String name) {
        this.name = name;
        this.imageToTextMap = new AssociativeArray<>();
    }

    /**
     * Adds the mapping of the image location to the text to the category.
     *
     * @param imageLoc the location of the image to add.
     * @param text the text that the image maps to.
     */
    public void addItem(String imageLoc, String text) {
        imageToTextMap.set(imageLoc, text);
    }

    /**
     * Returns the name of the category.
     *
     * @return the name of the category.
     */
    public String getCategory() {
        return name;
    }

    /**
     * Returns the text associated with the given image location in this category.
     *
     * @param imageLoc the location of the image.
     * @return the text associated with the image, or null if not found.
     */
    public Optional<String> getText(String imageLoc) {
        try {
            return Optional.ofNullable(imageToTextMap.get(imageLoc));
        } catch (KeyNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     * Determines if the provided image is stored in the category.
     *
     * @param imageLoc the location of the image.
     * @return true if it is in the category, false otherwise.
     */
    public boolean hasImage(String imageLoc) {
        return imageToTextMap.hasKey(imageLoc);
    }

    /**
     * Returns an array of all the images in the category.
     *
     * @return the array of image locations.
     */
    public String[] getImages() {
        return imageToTextMap.keys().stream().toArray(String[]::new);
    }
}
