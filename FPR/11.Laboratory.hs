import Distribution.SPDX (LicenseExpression(ELicense))
data Element
    = Button {name :: String, text :: String}
    | Text {text :: String}
    | Panel {children :: [Element]}
    deriving(Show)

exampleGUI :: Element
exampleGUI = Panel
    [ Panel
        [ Button "btn_new" "New"
        , Button "btn_save" "Save"
        , Button "btn_load" "Load"
        ]
    ,Panel
        [ Text "text Goes here" ]
    ,Panel []
    ]

listAllButtons :: Element -> [Element]
listAllButtons (Button name text) = [Button name text]
listAllButtons (Text _) = []
listAllButtons (Panel children) = listButtonsFromChildren children
    where
        listButtonsFromChildren :: [Element] -> [Element]
        listButtonsFromChildren [] = []
        listButtonsFromChildren (x:xs) = listAllButtons x ++ listButtonsFromChildren xs

removeAllButtons :: Element -> Element
removeAllButtons (Button _ _) = Panel []
removeAllButtons (Text text) = Text text
removeAllButtons (Panel children) = Panel (removeButtonsFromChildren children) 
    where
        removeButtonsFromChildren :: [Element] -> [Element]
        removeButtonsFromChildren [] = []
        removeButtonsFromChildren (x:xs) = removeAllButtons x :removeButtonsFromChildren xs


