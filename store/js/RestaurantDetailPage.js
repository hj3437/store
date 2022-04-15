import { request } from '/js/api.js'
import RestaurantDetailEdit from '/js/RestaurantDetailEdit.js'
import RestaurantDetailAdd from '/js/RestaurantDetailAdd.js'

const MAX_PAGE = 4

export default function RestaurantDetailPage({ $app, initialState }) {
    $app.innerHTML = ''

    this.state = initialState

    const $page = document.createElement('div')
    $page.className = 'StoreDetailPage'
    $app.appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }
    
    this.render = () => {        
        const { store } = this.state
        if (store) {

            const adminItemAddOption = ` <span class="add-menu">메뉴추가</span>`
            const adminItemEditOption = `
                <div class="edit-menu">
                    <span class="edit-button">편집</span>
                    <span class="delete-button">삭제</span>
                </div>
            `            

            $page.innerHTML = `
            <div class="StoreDetailTitle">
                <a href="/restaurants/${store.storeId}">
                    <img src="${store.storeImageUrl.indexOf('http') === 0 ? store.storeImageUrl : 'https://via.placeholder.com/48x48'}">  
                    <h3>${store.storeName} 메뉴</h1>
                </a>
            </div>
            <div>
                ${sessionStorage.getItem("store-admin")? adminItemAddOption :``}  
            </div>
            <ul>
                ${store.items.map(item => `
                <li data-item-id="${item.id}">
                    <img src="${item.imageUrl.indexOf('http') === 0 ? item.imageUrl : 'https://via.placeholder.com/140x120'}"> 
                    <div class="item-info">                        
                        <div>                            
                            <p>${item.name}</p>
                            <p><i>${item.price.toLocaleString('ko-KR')}</i> 원</p>
                        </div>                        
                        ${sessionStorage.getItem("store-admin")? adminItemEditOption :``}  
                    </div>                  
                </li>
                `).join('')}        
            </ul>
        `
        }
    }

    this.fetchRestaurantItem = async () => {
        if (this.state.restaurantId) {
            const store = await request(this.state.restaurantId, 'items')
            this.setState({ ...this.state, store: store })
        }

    }

    if (this.state.restaurantId) {
        this.fetchRestaurantItem()
    }

    this.render()

    $page.addEventListener('click', (e) => {
        if (e.target.closest('.edit-button')) {
            const { store } = this.state
            const { itemId } = e.target.closest('li').dataset

            if (store && itemId) {
                new RestaurantDetailEdit({
                    $app, initialState: {
                        storeId: store.storeId,
                        itemId: itemId
                    }
                })
            }
        }

        if (e.target.closest('.delete-button')) {
            const result = confirm('메뉴를 삭제하시겠습니까?')
            if (result) {
                const { store } = this.state
                const { itemId } = e.target.closest('li').dataset
                fetchDeleteMenu(store.storeId, itemId)
            }
        }

        if (e.target.closest('.add-menu')) {            
            const { store } = this.state
            if (store) {
                new RestaurantDetailAdd({
                    $app, initialState: {
                        storeId: store.storeId
                    }
                })
            }
        }
    })

    const fetchDeleteMenu = async (storeId, itemId) => {        
        const BASE_URL = 'https://dosorme.ga/api/restaurants'
        try {
            const options = {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
            }
            const response = await fetch(`${BASE_URL}/${storeId}/items/${itemId}`, options)
            if (response.ok) {
                location.reload()
                return response
            }
            throw new Error('Network Error')
        } catch (e) {
            console.error(e.message);
        }
    }
}