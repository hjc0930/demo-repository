export class Flip {
  private static START_POSITION = "_START_POSITION_";
  private container: HTMLElement | null;

  constructor(container: HTMLElement | null = null) {
    this.container = container;
  }

  public startRectOfrecalculate = () => {
    [...(this.container?.children ?? [])].forEach((item) => {
      Reflect.set(item, Flip.START_POSITION, item.getBoundingClientRect().top);
    });
  };

  public play = () => {
    [...(this.container?.children ?? [])].forEach((item) => {
      const startTop = Reflect.get(item, Flip.START_POSITION);
      const endTop = item.getBoundingClientRect().top;
      Reflect.set(item, Flip.START_POSITION, endTop);
      const offset = startTop - endTop;

      item.animate(
        [
          {
            transform: `translate(0, ${offset}px)`,
          },
          {
            transform: "translate(0, 0)",
          },
        ],
        {
          duration: 300,
        }
      );
    });
  };
}
