import { request } from '/js/api.js'

export default function RestaurantListAdd({ $app, initialState }) {
    $app.innerHTML = ''

    this.state = initialState

    const $page = document.createElement('div')
    $page.className = 'RestaurantListAdd'
    $app.appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }

    this.render = () => {
        $page.innerHTML = `
            <h3>스토어 추가하기</h3>
            <p>스토어 이름</p>
            <input id="storeName" type ='text' name='description' value="">
            <p>주소</p>
            <input id="storeAddress" type ='text' name='description' value="">                       
            <p>이미지 </p>
            <input id="storeImageUrl" type ='text' name='description' value="">             
            <div>
                <span class="add-button">저장</span>
                <a class="cancel-button" href=''>취소</a>
            </div>                      
            `
    }

    this.render()

    $page.addEventListener('click', (e) => {
        const updateButton = e.target.closest('.add-button')
        if (updateButton) {
            const name = $page.querySelector('#storeName').value
            const address = $page.querySelector('#storeAddress').value
            const imageUrl = $page.querySelector('#storeImageUrl').value
                
            if (name && address && imageUrl) {
                fetchStoreAdd(name, address, imageUrl)        
            }
        }
    })

    const fetchStoreAdd = async (name, address, imageUrl) => {        
        const BASE_URL = 'https://dosorme.ga/api/restaurants'
        try {
            const data = JSON.stringify({ name: name, address: address, imageUrl: imageUrl })
            const options = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: data
            }

            const response = await fetch(`${BASE_URL}`, options)

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