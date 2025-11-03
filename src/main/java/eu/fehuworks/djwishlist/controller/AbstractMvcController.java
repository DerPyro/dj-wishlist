package eu.fehuworks.djwishlist.controller;

public abstract class AbstractMvcController {

  private final String path;

  AbstractMvcController(String path) {
    this.path = path;
  }

  protected final String reloadPage() {
    return redirectTo(this.path);
  }

  protected final String redirectTo(String path) {
    return "redirect:" + path;
  }
}
