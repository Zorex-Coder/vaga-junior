/**
 * Modulo responsável por gerenciar a tela de Combustíveis.
 */
class CombustivelController {
    constructor() {
        this.endpoint = '/combustiveis';
    }

    async init() {
        this.bindElements();
        this.bindEvents();
        await this.loadData();
    }

    bindElements() {
        this.form = document.getElementById('form-combustivel');
        this.inputId = document.getElementById('combustivel-id');
        this.inputNome = document.getElementById('nome');
        this.inputPreco = document.getElementById('preco');
        this.tableBody = document.getElementById('lista-combustiveis');
        this.btnCancelar = document.getElementById('btn-cancelar');
    }

    bindEvents() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        this.btnCancelar.addEventListener('click', () => this.resetForm());
    }

    async loadData() {
        try {
            const data = await fetchApi(this.endpoint);
            this.renderTable(data);
        } catch (error) {
            alert('Erro ao carregar combustíveis: ' + error.message);
        }
    }

    renderTable(combustiveis) {
        this.tableBody.innerHTML = '';

        if (combustiveis.length === 0) {
            this.tableBody.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum registro encontrado.</td></tr>';
            return;
        }

        combustiveis.forEach(item => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>R$ ${item.preco.toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-edit" data-id="${item.id}" data-nome="${item.nome}" data-preco="${item.preco}">Editar</button>
                    <button class="btn btn-sm btn-delete" data-id="${item.id}">Excluir</button>
                </td>
            `;
            this.tableBody.appendChild(tr);
        });

        this.bindTableEvents();
    }

    bindTableEvents() {
        document.querySelectorAll('.btn-edit').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleEditClick(e.target));
        });

        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleDeleteClick(e.target.dataset.id));
        });
    }

    handleEditClick(button) {
        this.inputId.value = button.dataset.id;
        this.inputNome.value = button.dataset.nome;
        this.inputPreco.value = button.dataset.preco;
        this.btnCancelar.classList.remove('hidden');
    }

    async handleSubmit(e) {
        e.preventDefault();

        const payload = {
            nome: this.inputNome.value,
            preco: parseFloat(this.inputPreco.value)
        };

        const id = this.inputId.value;
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${this.endpoint}/${id}` : this.endpoint;

        try {
            await fetchApi(url, method, payload);

            this.resetForm();
            await this.loadData();
            alert('Salvo com sucesso!');
        } catch (error) {
            alert('Erro ao salvar: ' + error.message);
        }
    }

    async handleDeleteClick(id) {
        if (!confirm('Deseja realmente excluir este combustível?')) return;

        try {
            await fetchApi(`${this.endpoint}/${id}`, 'DELETE');

            await this.loadData();
            alert('Excluído com sucesso!');
        } catch (error) {
            alert('Erro ao excluir: ' + error.message);
        }
    }

    resetForm() {
        this.form.reset();
        this.inputId.value = '';
        this.btnCancelar.classList.add('hidden');
    }
}

// Exporta para ser usado pelo main.js
window.combustivelController = new CombustivelController();