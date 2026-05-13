const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Realiza chamadas HTTP para a API.
 * @param {string} endpoint - Caminho da API (ex: '/combustiveis').
 * @param {string} method - Verbo HTTP ('GET', 'POST', 'PUT', 'DELETE').
 * @param {Object} [body] - Corpo da requisição (opcional).
 * @returns {Promise<any>} Resposta em formato JSON.
 */
async function fetchApi(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json'
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(`Erro ${response.status}: ${errorData.message || 'Falha na requisição'}`);
        }

        // Retorno vazio para DELETE (204 No Content)
        if (response.status === 204) {
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error(`[API Error] ${method} ${endpoint}:`, error);
        throw error;
    }
}
