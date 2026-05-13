document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
});

function setupNavigation() {
    const navCombustiveis = document.getElementById('nav-combustiveis');
    const navBombas = document.getElementById('nav-bombas');
    const navAbastecimentos = document.getElementById('nav-abastecimentos');

    navCombustiveis.addEventListener('click', (e) => handleNavClick(e, renderCombustiveis));
    navBombas.addEventListener('click', (e) => handleNavClick(e, renderBombas));
    navAbastecimentos.addEventListener('click', (e) => handleNavClick(e, renderAbastecimentos));
}

function handleNavClick(event, renderFunction) {
    event.preventDefault();
    renderFunction();
}

function renderCombustiveis() {
    const content = document.getElementById('app-content');
    content.innerHTML = `
        <h2>Gerenciar Tipos de Combustível</h2>
        <p>Funcionalidade em desenvolvimento...</p>
    `;
    // Aqui faremos fetchApi('/combustiveis') futuramente
}

function renderBombas() {
    const content = document.getElementById('app-content');
    content.innerHTML = `
        <h2>Gerenciar Bombas</h2>
        <p>Funcionalidade em desenvolvimento...</p>
    `;
}

function renderAbastecimentos() {
    const content = document.getElementById('app-content');
    content.innerHTML = `
        <h2>Gerenciar Abastecimentos</h2>
        <p>Funcionalidade em desenvolvimento...</p>
    `;
}