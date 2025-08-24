data Komponenta
    = Tlacitko String String
    | TextovePole String String
    | Kontejner String [Komponenta]
    deriving (Show)

pridejPrvekDoKontejneru :: Komponenta -> Komponenta -> String -> Komponenta
pridejPrvekDoKontejneru strukturaGUI novyPrvek cilovyNazevKontejneru = pridejDoKontejneruRekurzivne strukturaGUI
  where
    pridejDoKontejneruRekurzivne (Kontejner aktualniNazev podkomponenty)
      | aktualniNazev == cilovyNazevKontejneru = Kontejner aktualniNazev (podkomponenty ++ [novyPrvek])
      | otherwise = Kontejner aktualniNazev (map pridejDoKontejneruRekurzivne podkomponenty)
    pridejDoKontejneruRekurzivne jinaKomponenta = jinaKomponenta
