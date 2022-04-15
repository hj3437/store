import { request } from '/js/api.js'


export default function RestaurantEditPage({ $app, initialState }) {
    $app.innerHTML = ''

    this.state = initialState

    const $page = document.createElement('div')
    $page.className = 'StoreEditlPage'
    $app.appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }

    this.render = () => {
        const { store } = this.state
        if (store) {
            $page.innerHTML = `
            <h3>메뉴 편집하기</h3>
            <p>메뉴명</p>
            <input id="storeName" type ='text' name='description' value="${store.name}">
            <p>가격</p>
            <input id="storePrice" type ='number' name='description' value="${store.price}">                       
            <p>이미지 주소</p>
            <input id="storeImageUrl" type ='text' name='description' value="${store.imageUrl}"> 
            <div>
                <span class="update-button">저장</span>                
                <a class="cancel-button" href=''>취소</a>            
            </div>            
            `
        }
    }

    this.fetchRestaurantItem = async () => {
        const store = await request(this.state.storeId, 'items', this.state.itemId)
        this.setState({ ...this.state, store: store })
    }

    if (this.state.storeId) {
        this.fetchRestaurantItem()
    }

    this.render()

    $page.addEventListener('click', (e) => {
        const updateButton = e.target.closest('.update-button')
        if (updateButton) {
            const name = $page.querySelector('#storeName').value
            const price = $page.querySelector('#storePrice').value
            const imageUrl = $page.querySelector('#storeImageUrl').value

            const { storeId, itemId } = this.state

            editMenu(storeId, itemId, name, price, imageUrl)            
        }      
    })

    const editMenu = async (storeId, itemId, name, price, imageUrl) => {        
        const BASE_URL = 'https://dosorme.ga/api/restaurants'
        try {
            const menuData = JSON.stringify({ name: name, price: price, imageUrl: imageUrl })
            const options = {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: menuData
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