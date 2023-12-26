import { Configuration as WebpackConfiguration } from "webpack";
import { Configuration as WebpackServerConfiguration } from "webpack-dev-server";

export = FastCli;
export as namespace FastCli;

declare namespace FastCli {
  interface FastConfig extends Omit<WebpackConfiguration, "devServer"> {
    server?: WebpackServerConfiguration;
  }

  type Callback = (
    mode: "development" | "production",
    env: Record<string, string>
  ) => FastConfig;

  function defineConfig(config: FastConfig): FastConfig;
  function defineConfig(callback: Callback): Callback;

  class ModuleFederationPlugin {
    constructor();
  }
}
