type Bludiste = [String]

slouceniBludistVedleSebe :: Bludiste -> Bludiste -> Bludiste
slouceniBludistVedleSebe leveBludiste praveBludiste = map (\(radekLevy, radekPravy) -> radekLevy ++ radekPravy) (zip leveBludiste praveBludiste)
