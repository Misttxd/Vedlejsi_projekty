data Attribute = Attribute
    { name  :: String  -- Name of the attribute
    , value :: String  -- Value of the attribute
    }
    deriving (Show)

-- Data structure to represent a Tag
data Tag = Tag
    { tagName    :: String        -- Name of the tag
    , attributes :: [Attribute]   -- List of attributes for the tag
    , children   :: [Tag]         -- List of nested tags (children)
    }
    deriving (Show)

-- Data structure to represent the HTMLDocument
data HTMLDocument = HTMLDocument
    { tags :: [Tag]  -- Top-level tags in the document
    }
    deriving (Show)


exampleHTML :: HTMLDocument
exampleHTML = HTMLDocument
    [ Tag "html" []  
        [ Tag "head" []  
            [ Tag "title" []  
                [ Tag "text" [] []  
                ]
            ]
        , Tag "body" [Attribute "class" "main"]  
            [ Tag "h1" [Attribute "id" "header"]  
                [ Tag "text" [] []  
                ]
            , Tag "p" []  
                [ Tag "text" [] []  
                ]
            ]
        ]
    ]


data Component 
    = TextBox {compName :: String, compText :: String}
    | Button  {compName :: String, compValue :: String}
    | Container {compName:: String, compChildren :: [Component]}
    deriving(Show)

gui :: Component
gui = Container "My App"
        [ Container "Menu"
            [ Button "btn_new" "New"
            , Button "btn_open" "Open"
            , Button "btn_close" "Close"
            ]
        , Container "Body"
            [ TextBox "textbox_1" "Some text goes here" ]
        , Container "Footer" []
        ]

listButtonNames :: Component -> [String]
listButtonNames (Button name _) = [name]
listButtonNames (TextBox _ _) = []
listButtonNames (Container _ children) = listButtonNamesInList children
    where
        listButtonNamesInList :: [Component]-> [String]
        listButtonNamesInList [] = []
        listButtonNamesInList (x:xs) = listButtonNames x ++ listButtonNamesInList xs


changeText :: Component -> String -> String -> Component
changeText (TextBox name oldText) targetName newText
    | name == targetName = TextBox name newText  -- If the name matches, update the text
    | otherwise = TextBox name oldText          -- Otherwise, return unchanged
changeText (Button name value) _ _ = Button name value  -- Buttons are not affected
changeText (Container name children) targetName newText =
    Container name (map (\child -> changeText child targetName newText) children)