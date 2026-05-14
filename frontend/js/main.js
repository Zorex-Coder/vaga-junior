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

async function renderCombustiveis() {
    const content = document.getElementById('app-content');

    // Busca o HTML do template
    try {
        const response = await fetch('pages/combustivel/template.html');
        const html = await response.text();
        content.innerHTML = html;

        // Inicializa o controlador
        if(window.combustivelController) {
            window.combustivelController.init();
        }
    } catch(err) {
        content.innerHTML = '<p>Erro ao carregar a página.</p>';
    }
}

async function renderBombas() {
    const content = document.getElementById('app-content');

    try {
        const response = await fetch('pages/bomba/template.html');
        const html = await response.text();
        content.innerHTML = html;

        if(window.bombaController) {
            window.bombaController.init();
        }
    } catch(err) {
        content.innerHTML = '<p>Erro ao carregar a página de bombas.</p>';
    }
}

async function renderAbastecimentos() {
    const content = document.getElementById('app-content');

    try {
        const response = await fetch('pages/abastecimento/template.html');
        const html = await response.text();
        content.innerHTML = html;

        if(window.abastecimentoController) {
            window.abastecimentoController.init();
        }
    } catch(err) {
        content.innerHTML = '<p>Erro ao carregar a página de abastecimentos.</p>';
    }
}