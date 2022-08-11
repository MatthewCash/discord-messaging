{
    description = "Discord Messaging";

    inputs = {
        nixpkgs.url = "nixpkgs/nixos-unstable";

        flake-utils = {
            url = "github:numtide/flake-utils";
            inputs.nixpkgs.follows = "nixpkgs";
        };
    };

    outputs = inputs @ { self, nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
        let pkgs = nixpkgs.legacyPackages.${system}; in rec {
            devShell = pkgs.mkShell rec {
                name = "discord-messaging";
                packages = with pkgs; [
                    gradle
                ];
            };
        }
    );
}