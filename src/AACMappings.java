import structures.AssociativeArray;
import structures.KeyNotFoundException;
import java.io.*;
import java.util.Optional;

public class AACMappings {
  private AssociativeArray<String, AACCategory> categoryMap;
  private String currentCategory;

  public AACMappings(String filename) {
    this.categoryMap = new AssociativeArray<>();
    this.currentCategory = ""; 
    loadMappingsFromFile(filename);
  }

  private void loadMappingsFromFile(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      AACCategory currentCat = null;
      while ((line = reader.readLine()) != null) {
        if (!line.startsWith(">")) {
          String[] parts = line.split(" ", 2);
          currentCat = new AACCategory(parts[1]);
          categoryMap.set(parts[0], currentCat);
        } else if (currentCat != null) {
          line = line.substring(1);
          String[] parts = line.split(" ", 2);
          currentCat.addItem(parts[0], parts[1]);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void add(String imageLoc, String text) {
    if (currentCategory.isEmpty()) {
      System.out.println("No current category set. Please set a current category before adding items.");
      return;
    }

    try {
      AACCategory category = categoryMap.get(currentCategory);
      category.addItem(imageLoc, text);
    } catch (KeyNotFoundException e) {
      System.err.println("Category not found: " + e.getMessage());
    }
  }
  public String getCurrentCategory() {
    return currentCategory;
}
  public String[] getImageLocs() {
    if (currentCategory.isEmpty()) {
      return new String[]{};
    }
    try {
      AACCategory category = categoryMap.get(currentCategory);
      return category.getImages();
    } catch (KeyNotFoundException e) {
      System.err.println("Failed to get images: " + e.getMessage());
      return new String[]{};
    }
  }

  public String getText(String imageLoc) {
    for (String key : categoryMap.keys()) {
        try {
            AACCategory cat = categoryMap.get(key);
            if (cat.hasImage(imageLoc)) {
                Optional<String> textOptional = cat.getText(imageLoc);
                if (textOptional.isPresent()) {
                    return textOptional.get(); // Return the text if it is present
                } else {
                    return ""; // Return an empty string if the text is not present
                }
            }
        } catch (KeyNotFoundException e) {
            System.err.println("Error finding category for image: " + e.getMessage());
        }
    }
    return ""; // Image not found in any category
}


  public boolean isCategory(String imageLoc) {
    return categoryMap.hasKey(imageLoc);
  }

  public void reset() {
    currentCategory = "";
  }

  public void writeToFile(String filename) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      for (String key : categoryMap.keys()) {
        try {
          AACCategory cat = categoryMap.get(key);
          writer.write(key + " " + cat.getCategory());
          writer.newLine();
          String[] images = cat.getImages();
          for (String imgLoc : images) {
            writer.write(">" + imgLoc + " " + cat.getText(imgLoc));
            writer.newLine();
          }
        } catch (KeyNotFoundException e) {
          System.err.println("Category not found while writing to file: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.err.println("Failed to write mappings to file: " + e.getMessage());
    }
  }
}
