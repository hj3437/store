import { request } from '/js/api.js'
import { onClick } from "/js/clickListener.js"
import RestaurantListEdit from '/js/RestaurantListEdit.js'
import RestaurantListAdd from '/js/RestaurantListAdd.js'

const MAX_PAGE = 4
const PAGE_RANGE = 4

export default function RestaurantListPage({ $app }) {
    $app.innerHTML = ''

    this.state = {}

    const $page = document.createElement('div')
    $page.className = 'StoresPage'
    $app.appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }

    this.render = () => {
        const { restaurants, currentPage } = this.state
        if (restaurants && currentPage) {
            const displayRestaurantList = restaurants.slice((currentPage - 1) * MAX_PAGE, currentPage * MAX_PAGE)
            const adminStoreAddOption = `<span class="store-add">스토어 추가</span>`
            const adminStoreEditOption = `
                <div>
                    <span class="store-edit">편집</span>
                    <span class="store-delete">삭제</span>
                </div>
            `
            $page.innerHTML = `
                ${sessionStorage.getItem("store-admin") ? adminStoreAddOption : ``}  
                <ul>
                    ${displayRestaurantList.map(restaurant => {
                return `                
                                <li data-restaurant-id="${restaurant.id}">
                                <img src="${restaurant.imageUrl.indexOf('http') === 0 ? restaurant.imageUrl : 'https://via.placeholder.com/210x120'}">                                                    
                                <div class="store-wrapper">
                                    <div>                           
                                        <h4>${restaurant.name}</h4>
                                        <span>${restaurant.address}</span>
                                    </div>
                                    ${sessionStorage.getItem("store-admin") ? adminStoreEditOption : ``}                                
                                </div>
                            </li>
                        `
            }).join('')}                                    
                </ul>
                <div class="store-index">
                    <ul> 
                        ${this.getStorePages()}                  
                    </ul>
                </div>
            `
        }
    }

    this.getStorePages = () => {
        const { currentPage, pageCount } = this.state
        let page = `<li><span>총 ${pageCount} 페이지</span></li>`
        if (pageCount && currentPage) {
            if (pageCount <= PAGE_RANGE) {
                for (let index = 1; index <= pageCount; index++) {
                    if (index == currentPage) {
                        page += `
                        <li data-board-page="${index}">
                            <span class="underline">${index}</span>
                        </li>
                        `
                    } else {
                        page += `
                    <li data-board-page="${index}">
                        <span>${index}</span>
                    </li>
                    `
                    }
                }
            } else {
                let isPrevRender = false
                let isNextRender = false
                const { pageIndex } = this.state

                if (pageIndex) {
                    for (let index = pageIndex.first; index <= pageIndex.last; index++) {
                        if (!isPrevRender && index > PAGE_RANGE) {
                            page += `
                            <li class="page-prev">
                                <span><</span>
                            </li>`
                            isPrevRender = true
                        }

                        if (index == currentPage) {
                            page += `                                                
                            <li data-board-page="${index}">
                                <span class="underline">${index}</span>
                            </li>
                            `
                        } else {
                            page += `
                                <li data-board-page="${index}">
                                    <span>${index}</span>
                                </li>
                              `
                        }

                        if (!isNextRender && index === pageIndex.last && pageIndex.first < pageIndex.last && pageCount !== pageIndex.last) {
                            page += `
                            <li class="page-next">
                                <span>></span>
                            </li>`
                            isNextRender = true
                        }
                    }
                } else {
                    for (let index = 1; index <= PAGE_RANGE; index++) {
                        if (!isPrevRender && index > PAGE_RANGE) {
                            page += `
                            <li class="page-prev">
                                <span><</span>
                            </li>`
                            isPrevRender = true
                        }

                        if (index == currentPage) {
                            page += `                                                
                            <li data-board-page="${index}">
                                <span class="underline">${index}</span>
                            </li>
                            `
                        } else {
                            page += `
                                <li data-board-page="${index}">
                                    <span>${index}</span>
                                </li>
                              `
                        }

                        if (!isNextRender && index === PAGE_RANGE) {
                            page += `
                            <li class="page-next">
                                <span>></span>
                            </li>`
                            isNextRender = true
                        }
                    }
                }
            }
        }

        return page
    }

    this.calculateStorePageNumber = (length) => {
        if (length <= MAX_PAGE) {
            return 1
        } else {            
            return Math.ceil(length / MAX_PAGE)
        }
    }

    this.fetchRestaurants = async () => {
        try {
            const restaurants = await request()
            const page = this.calculateStorePageNumber(restaurants.length)
            this.setState({ ...this.state, restaurants: restaurants, currentPage: 1, pageCount: page })
        } catch (e) {
            console.log(e.message);
        }
    }

    this.fetchRestaurants()
    this.render()

    $page.addEventListener('click', (e) => {
        const li = e.target.closest('li')

        if (li) {
            const { restaurantId } = li.dataset

            if (e.target.closest('.store-edit')) {
                new RestaurantListEdit({ $app, initialState: { restaurantId: restaurantId } })
                return
            }

            if (e.target.closest('.store-delete')) {
                const result = confirm('메뉴를 삭제하시겠습니까?')
                if (result) {
                    fetchStoreDelete(restaurantId)
                }
                return
            }

            if (restaurantId) {
                onClick(`/restaurants/${restaurantId}`)
            }
        }

        if (e.target.closest('.store-add')) {
            new RestaurantListAdd({ $app })
        }

        if (e.target.closest('.store-index')) {
            const li = e.target.closest('.store-index li')
            const { boardPage } = li.dataset
            this.setState({ ...this.state, currentPage: boardPage })
        }

        if (e.target.closest('.page-prev')) {            
            const prevli = e.target.closest('.store-index ul li').nextElementSibling
            const { boardPage } = prevli.dataset            
            if (parseInt(boardPage) === 1) {
                alert('첫번쨰 페이지입니다.')
                return
            } else {
                let targetIndex = parseInt(boardPage)
                let firstIndex = 0
                let lastIndex = 0

                if (targetIndex - PAGE_RANGE >= 1) {
                    firstIndex = targetIndex - PAGE_RANGE
                }

                if (targetIndex - 1 >= 1) {
                    lastIndex = targetIndex - 1
                }

                const pageIndex = {
                    first: firstIndex,
                    last: lastIndex
                }

                this.setState({ ...this.state, currentPage: firstIndex, pageIndex: pageIndex })
            }
        }

        if (e.target.closest('.page-next')) {            
            const li = e.target.closest('.store-index ul li').previousElementSibling
            const { boardPage } = li.dataset
            const { pageCount } = this.state

            let targetIndex = parseInt(boardPage)
            let firstIndex = targetIndex + 1
            let lastIndex = 0

            if (targetIndex === pageCount) {
                firstIndex = boardPage
                alert('마지막 페이지입니다.')
                return
            }

            if (pageCount > targetIndex + PAGE_RANGE) {
                lastIndex = targetIndex + PAGE_RANGE
            } else {
                lastIndex = pageCount
            }

            const pageIndex = {
                first: firstIndex,
                last: lastIndex
            }

            this.setState({ ...this.state, currentPage: firstIndex, pageIndex: pageIndex })
        }
    })

    const fetchStoreDelete = async (storeId) => {
        const BASE_URL = 'https://dosorme.ga/api/restaurants'
        try {
            const options = {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
            }
            const response = await fetch(`${BASE_URL}/${storeId}`, options)
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