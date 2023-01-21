{
    inputs = {
        nixpkgs.url = "github:NixOS/nixpkgs/nixos-22.11";
    };

    outputs = {self, nixpkgs, flake-parts}: let
        forEachSystem = systems: callback: nixpkgs.lib.genAttrs systems (system:
            let pkgs = nixpkgs.legacyPackages.${system}; in
            callback system pkgs
        );

        defaultSystems = ["aarch64-darwin"];
    in
    {
        devShells = forEachSystem defaultSystems (system: pkgs: {
            default = pkgs.mkShell {
                packages = with pkgs; [
                    nodejs
                    nodePackages.yarn
                ];
            };
        });
    };
}