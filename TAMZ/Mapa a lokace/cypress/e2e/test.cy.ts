describe('Mapa a lokace', () => {
  it('zobrazí úvodní obrazovku aplikace', () => {
    cy.visit('/')
    cy.contains('Mapa a lokace')
  })
})
