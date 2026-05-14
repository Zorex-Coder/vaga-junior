/**
 * Modulo responsável por gerenciar a tela de Bombas de Combustível.
 */
class BombaController {
    constructor() {
        this.endpoint = '/bombas';
    }

    async init() {
        this.bindElements();
        this.bindEvents();
        await this.loadCombustiveis();
        await this.loadData();
    }

    bindElements() {
        this.form = document.getElementById('form-bomba');
        this.inputId = document.getElementById('bomba-id');
        this.inputNome = document.getElementById('nome-bomba');
        this.selectCombustivel = document.getElementById('tipo-combustivel');
        this.tableBody = document.getElementById('lista-bombas');
        this.btnCancelar = document.getElementById('btn-cancelar-bomba');
    }

    bindEvents() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        this.btnCancelar.addEventListener('click', () => this.resetForm());
    }

    async loadCombustiveis() {
        try {
            // TODO: Descomentar quando a API estiver pronta
            // const combustiveis = await fetchApi('/combustiveis');

            // Mock temporário para testar
            const mockCombustiveis = [
                { id: 1, nome: 'Gasolina Comum' },
                { id: 2, nome: 'Etanol' }
            ];

            this.populateCombustiveisSelect(mockCombustiveis);
        } catch (error) {
            alert('Erro ao carregar lista de combustíveis: ' + error.message);
        }
    }

    populateCombustiveisSelect(combustiveis) {
        // Remove opções antigas (mantendo o placeholder)
        this.selectCombustivel.innerHTML = '<option value="">Selecione um combustível...</option>';

        combustiveis.forEach(c => {
            const option = document.createElement('option');
            option.value = c.id;
            option.textContent = c.nome;
            this.selectCombustivel.appendChild(option);
        });
    }

    async loadData() {
        try {
            // TODO: Descomentar quando a API estiver pronta
            // const data = await fetchApi(this.endpoint);

            // Mock temporário para testar a tela
            const mockData = [
                { id: 1, nome: 'Bomba 01', combustivelId: 1, combustivelNome: 'Gasolina Comum' },
                { id: 2, nome: 'Bomba 02', combustivelId: 2, combustivelNome: 'Etanol' }
            ];
            this.renderTable(mockData);
        } catch (error) {
            alert('Erro ao carregar bombas: ' + error.message);
        }
    }

    renderTable(bombas) {
        this.tableBody.innerHTML = '';

        if (bombas.length === 0) {
            this.tableBody.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum registro encontrado.</td></tr>';
            return;
        }

        bombas.forEach(item => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>${item.combustivelNome}</td>
                <td>
                    <button class="btn btn-sm btn-edit-bomba" data-id="${item.id}" data-nome="${item.nome}" data-combustivel="${item.combustivelId}">Editar</button>
                    <button class="btn btn-sm btn-delete-bomba" data-id="${item.id}">Excluir</button>
                </td>
            `;
            this.tableBody.appendChild(tr);
        });

        this.bindTableEvents();
    }

    bindTableEvents() {
        document.querySelectorAll('.btn-edit-bomba').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleEditClick(e.target));
        });

        document.querySelectorAll('.btn-delete-bomba').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleDeleteClick(e.target.dataset.id));
        });
    }

    handleEditClick(button) {
        this.inputId.value = button.dataset.id;
        this.inputNome.value = button.dataset.nome;
        this.selectCombustivel.value = button.dataset.combustivel;
        this.btnCancelar.classList.remove('hidden');
    }

    async handleSubmit(e) {
        e.preventDefault();

        const payload = {
            nome: this.inputNome.value,
            combustivelId: parseInt(this.selectCombustivel.value, 10)
        };

        const id = this.inputId.value;
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${this.endpoint}/${id}` : this.endpoint;

        try {
            // TODO: Descomentar quando a API estiver pronta
            // await fetchApi(url, method, payload);
            console.log(`Simulando API -> ${method} ${url}`, payload);

            this.resetForm();
            await this.loadData();
            alert('Bomba salva com sucesso! (Simulação)');
        } catch (error) {
            alert('Erro ao salvar bomba: ' + error.message);
        }
    }

    async handleDeleteClick(id) {
        if (!confirm('Deseja realmente excluir esta bomba?')) return;

        try {
            // TODO: Descomentar quando a API estiver pronta
            // await fetchApi(`${this.endpoint}/${id}`, 'DELETE');
            console.log(`Simulando API -> DELETE ${this.endpoint}/${id}`);

            await this.loadData();
            alert('Bomba excluída com sucesso! (Simulação)');
        } catch (error) {
            alert('Erro ao excluir bomba: ' + error.message);
        }
    }

    resetForm() {
        this.form.reset();
        this.inputId.value = '';
        this.btnCancelar.classList.add('hidden');
    }
}

// Exporta para ser usado pelo main.js
window.bombaController = new BombaController();