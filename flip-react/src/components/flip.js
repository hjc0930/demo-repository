export class Flip {
  #duration;
  #startLocations = new Map();

  constructor(elements, duration = 300) {
    this.#duration = duration;

    if (
      !elements ||
      Object.prototype.toString.call(elements) !== "[object HTMLCollection]"
    )
      return;

    for (let i = 0; i < elements.length; i++) {
      this.#startLocations.set(elements[i], this.#getLocation(elements[i]));
    }
  }

  #getLocation = (element) => {
    const rect = element.getBoundingClientRect();
    return rect.left;
  };

  play = () => {
    for (const [element, start] of this.#startLocations) {
      const dis = start - this.#getLocation(element);
      if (dis === 0) {
        continue;
      }

      element.animate(
        [
          {
            transform: `translateX(${dis}px)`,
          },
          {
            transform: "none",
          },
        ],
        {
          duration: this.#duration,
        }
      );
    }
  };
}
