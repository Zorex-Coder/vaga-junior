/**
 * Modulo responsável por gerenciar a tela de Abastecimentos.
 */
class AbastecimentoController {
    constructor() {
        this.endpoint = '/abastecimentos';
    }

    async init() {
        this.bindElements();
        this.bindEvents();
        await this.loadBombas();
        await this.loadData();
    }

    bindElements() {
        this.form = document.getElementById('form-abastecimento');
        this.inputId = document.getElementById('abastecimento-id');
        this.selectBomba = document.getElementById('bomba-abastecimento');
        this.inputData = document.getElementById('data-abastecimento');
        this.inputLitragem = document.getElementById('litragem');
        this.inputValor = document.getElementById('valor-total');
        this.tableBody = document.getElementById('lista-abastecimentos');
        this.btnCancelar = document.getElementById('btn-cancelar-abastecimento');
    }

    bindEvents() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        this.btnCancelar.addEventListener('click', () => this.resetForm());
        // Simular o preenchimento automático da data no momento de criar um novo
        if (!this.inputId.value) {
            this.inputData.value = new Date().toISOString().slice(0, 16);
        }
    }

    async loadBombas() {
        try {
            const bombas = await fetchApi('/bombas');
            this.populateBombasSelect(bombas);
        } catch (error) {
            alert('Erro ao carregar lista de bombas: ' + error.message);
        }
    }

    populateBombasSelect(bombas) {
        this.selectBomba.innerHTML = '<option value="">Selecione uma bomba...</option>';

        bombas.forEach(b => {
            const option = document.createElement('option');
            option.value = b.id;
            option.textContent = b.nome;
            this.selectBomba.appendChild(option);
        });
    }

    async loadData() {
        try {
            const data = await fetchApi(this.endpoint);
            this.renderTable(data);
        } catch (error) {
            alert('Erro ao carregar abastecimentos: ' + error.message);
        }
    }

    renderTable(abastecimentos) {
        this.tableBody.innerHTML = '';

        if (abastecimentos.length === 0) {
            this.tableBody.innerHTML = '<tr><td colspan="6" class="text-center">Nenhum registro encontrado.</td></tr>';
            return;
        }

        abastecimentos.forEach(item => {
            const dataFormatada = new Date(item.data).toLocaleString('pt-BR');
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${item.id}</td>
                <td>${dataFormatada}</td>
                <td>${item.bombaNome}</td>
                <td>${item.litragem.toFixed(2)}</td>
                <td>R$ ${item.valor.toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-edit-abastecimento" data-id="${item.id}" data-bomba="${item.bombaId}" data-data="${item.data}" data-litragem="${item.litragem}" data-valor="${item.valor}">Editar</button>
                    <button class="btn btn-sm btn-delete-abastecimento" data-id="${item.id}">Excluir</button>
                </td>
            `;
            this.tableBody.appendChild(tr);
        });

        this.bindTableEvents();
    }

    bindTableEvents() {
        document.querySelectorAll('.btn-edit-abastecimento').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleEditClick(e.target));
        });

        document.querySelectorAll('.btn-delete-abastecimento').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleDeleteClick(e.target.dataset.id));
        });
    }

    handleEditClick(button) {
        this.inputId.value = button.dataset.id;
        this.selectBomba.value = button.dataset.bomba;
        this.inputData.value = button.dataset.data;
        this.inputLitragem.value = button.dataset.litragem;
        this.inputValor.value = button.dataset.valor;
        this.btnCancelar.classList.remove('hidden');
    }

    async handleSubmit(e) {
        e.preventDefault();

        const payload = {
            bombaId: parseInt(this.selectBomba.value, 10),
            data: this.inputData.value,
            litragem: parseFloat(this.inputLitragem.value),
            valorTotal: parseFloat(this.inputValor.value)
        };

        const id = this.inputId.value;
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${this.endpoint}/${id}` : this.endpoint;

        try {
            await fetchApi(url, method, payload);

            this.resetForm();
            await this.loadData();
            alert('Abastecimento salvo com sucesso!');
        } catch (error) {
            alert('Erro ao salvar abastecimento: ' + error.message);
        }
    }

    async handleDeleteClick(id) {
        if (!confirm('Deseja realmente excluir este abastecimento?')) return;

        try {
            await fetchApi(`${this.endpoint}/${id}`, 'DELETE');

            await this.loadData();
            alert('Abastecimento excluído com sucesso!');
        } catch (error) {
            alert('Erro ao excluir abastecimento: ' + error.message);
        }
    }

    resetForm() {
        this.form.reset();
        this.inputId.value = '';
        this.inputData.value = new Date().toISOString().slice(0, 16);
        this.btnCancelar.classList.add('hidden');
    }
}

// Exporta para ser usado pelo main.js
window.abastecimentoController = new AbastecimentoController();