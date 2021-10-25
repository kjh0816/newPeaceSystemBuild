package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.*
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.client.Client
import com.base.newPeaceSystemBuild.vo.client.Family
import com.base.newPeaceSystemBuild.vo.member.Member
import com.base.newPeaceSystemBuild.vo.standard.*
import com.base.newPeaceSystemBuild.vo.vendor.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartRequest
import java.text.DecimalFormat
import kotlin.math.ceil


@Controller
class UsrDirectorController(
    private val memberService: MemberService,
    private val genFileService: GenFileService,
    private val memberRoleService: MemberRoleService,
    private val vendorService: VendorService,
    private val clientService: ClientService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/usr/director/request")
    fun showRequest(): String {
        return "usr/director/request"
    }

    @RequestMapping("/usr/director/modify")
    fun showModify(): String {
        return "usr/director/modify"
    }

    @RequestMapping("/usr/director/jusoPopup")
    fun showJusoPopup(
            model: Model,
            @RequestParam(defaultValue = "") inputYn: String,
            @RequestParam(defaultValue = "") roadFullAddr: String
    ): String{

        if(inputYn.isNotBlank() && roadFullAddr.isNotBlank()){
            model.addAttribute("inputYn", inputYn)
            model.addAttribute("roadFullAddr", roadFullAddr)
        }

        // 보안을 위해 서버 사이드에서 API 승인 key를 넘겨줌
        model.addAttribute("confmKey", "devU01TX0FVVEgyMDIxMTAwNjE0NTM0NTExMTcyNjM=")
        // 검색결과 화면 출력유(1 : 도로명, 2 : 도로명+지번, 3 : 도로명+상세건물명, 4 : 도로명+지번+상세건물명)
        model.addAttribute("resultType", "1")

        return "usr/director/jusoPopup"
    }

    @RequestMapping("/usr/director/progress")
    fun showProgress(
        model: Model,
        @RequestParam(defaultValue = "1") page: Int
    ): String {
        val funerals = clientService.getFuneralsByDirectorMemberIdAndProgress(rq.getLoginedMember()!!.id, true)
        // 페이징
        val itemsInAPage = 1
        val limitFrom = (page - 1) * itemsInAPage
        val filteredFunerals = clientService.getFilteredFuneralsByDirectorMemberIdAndProgress(rq.getLoginedMember()!!.id, true, page, itemsInAPage, limitFrom)
        val totalPage = ceil(funerals.size.toDouble() / itemsInAPage).toInt()

        val clients = mutableListOf<Client>()
        val chiefs = mutableListOf<Family>()
        // 가격정보
        val flowerPrices = mutableListOf<String>()
        val flowerTributePrices = mutableListOf<String>()
        val femaleMourningClothBlackPrices = mutableListOf<String>()
        val femaleMourningClothWhitePrices = mutableListOf<String>()
        val maleMourningClothPrices = mutableListOf<String>()
        val shirtPrices = mutableListOf<String>()
        val necktiePrices = mutableListOf<String>()
        val shroudPrices = mutableListOf<String>()
        val coffinPrices = mutableListOf<String>()
        // 가격 합계
        val sums = mutableListOf<String>()
        // 스탠다드
        val flowers = mutableListOf<Flower?>()
        val flowerTributes = mutableListOf<FlowerTribute?>()
        val femaleMourningClothBlacks = mutableListOf<MourningCloth?>()
        val femaleMourningClothWhites = mutableListOf<MourningCloth?>()
        val maleMourningCloths = mutableListOf<MourningCloth?>()
        val shirts = mutableListOf<MourningCloth?>()
        val neckties = mutableListOf<MourningCloth?>()
        val shrouds = mutableListOf<Shroud?>()
        val coffins = mutableListOf<Coffin?>()
        // 주문정보
        val flowerOrders = mutableListOf<Order?>()
        val flowerTributeOrders = mutableListOf<Order?>()
        val femaleMourningClothBlackOrders = mutableListOf<Order?>()
        val femaleMourningClothWhiteOrders = mutableListOf<Order?>()
        val maleMourningClothOrders = mutableListOf<Order?>()
        val shirtOrders = mutableListOf<Order?>()
        val necktieOrders = mutableListOf<Order?>()
        val shroudOrders = mutableListOf<Order?>()
        val coffinOrders = mutableListOf<Order?>()
        // 운구차 업자 정보
        val coffinTransporters = mutableListOf<CoffinTransporter?>()
        val coffinTransporterMembers = mutableListOf<Member?>()
        val coffinTransporterCellphoneNos = mutableListOf<String?>()

        // 숫자 변환기
        val formatter = DecimalFormat("###,###")
        for (funeral in filteredFunerals){
            // 상품 가격정보
            var flowerPrice = 0
            var flowerTributePrice = 0
            var femaleMourningClothBlackPrice = 0
            var femaleMourningClothWhitePrice = 0
            var maleMourningClothPrice = 0
            var shirtPrice = 0
            var necktiePrice = 0
            var shroudPrice = 0
            var coffinPrice = 0

            // 합계
            var sum = 0

            // 장례별 선택된 스텐다드 데이터
            val flower = vendorService.getFlowerById(funeral.flowerId)
            val flowerTribute = vendorService.getFlowerTributeById(funeral.flowerTributeId)
            val femaleMourningClothBlack = vendorService.getFemaleMourningClothBlackById(funeral.femaleMourningClothBlackId)
            val femaleMourningClothWhite = vendorService.getFemaleMourningClothWhiteById(funeral.femaleMourningClothWhiteId)
            val maleMourningCloth = vendorService.getMaleMourningClothById(funeral.maleMourningClothId)
            val shirt = vendorService.getShirtById(funeral.shirtId)
            val necktie = vendorService.getNecktieById(funeral.necktieId)
            val shroud = vendorService.getShroudById(funeral.shroudId)
            val coffin = vendorService.getCoffinById(funeral.coffinId)

            // 운구차 데이터
            val coffinTransporter = vendorService.getCoffinTransporterByFuneralId(funeral.id)
            var coffinTransporterMember: Member? = null
            var coffinTransporterCellphoneNo: String? = null

            if(coffinTransporter != null){
                // coffinTransporter의 운구업자가 등록되었을 경우, 추가적으로 운구업자의 정보를 addAttr 한다.
                coffinTransporterMember = memberService.getMemberById(coffinTransporter.memberId)
                if(coffinTransporterMember != null){
                    coffinTransporterCellphoneNo = Ut.getCellphoneNoFormatted(coffinTransporterMember.cellphoneNo)
                }

            }
            // 운구차 데이터를 배열에 넣어줌
            coffinTransporters.add(coffinTransporter)
            coffinTransporterMembers.add(coffinTransporterMember)
            coffinTransporterCellphoneNos.add(coffinTransporterCellphoneNo)

            // 선택된 스텐다드 데이터를 배열에 넣어줌
            flowers.add(flower)
            flowerTributes.add(flowerTribute)
            femaleMourningClothBlacks.add(femaleMourningClothBlack)
            femaleMourningClothWhites.add(femaleMourningClothWhite)
            maleMourningCloths.add(maleMourningCloth)
            shirts.add(shirt)
            neckties.add(necktie)
            shrouds.add(shroud)
            coffins.add(coffin)

            // 장례별 선택된 스텐다드 데이터의 주문 상세정보
            val flowerOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "flower",
                funeral.id
            )
            val flowerTributeOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "flowerTribute",
                funeral.id
            )
            val femaleMourningClothBlackOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "femaleMourningClothBlack",
                funeral.id
            )
            val femaleMourningClothWhiteOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "femaleMourningClothWhite",
                funeral.id
            )
            val maleMourningClothOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "maleMourningCloth",
                funeral.id
            )
            val shirtOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "shirt",
                funeral.id
            )
            val necktieOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "necktie",
                funeral.id
            )
            val shroudOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                false,
                "shroud",
                funeral.id
            )
            val coffinOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(
                    false,
                    "coffin",
                    funeral.id
            )

            // 장레별 선택된 주문의 상세정보를 배열에 넣어줌
            flowerOrders.add(flowerOrder)
            flowerTributeOrders.add(flowerTributeOrder)
            femaleMourningClothBlackOrders.add(femaleMourningClothBlackOrder)
            femaleMourningClothWhiteOrders.add(femaleMourningClothWhiteOrder)
            maleMourningClothOrders.add(maleMourningClothOrder)
            shirtOrders.add(shirtOrder)
            necktieOrders.add(necktieOrder)
            shroudOrders.add(shroudOrder)
            coffinOrders.add(coffinOrder)

            // 주문된 상품들의 합계랑 각각의 상품들의 가격을 계산해줌
            if (flower != null) {
                flowerPrice = flower.retailPrice.toInt()
                sum += flowerPrice
                flowerPrices.add(formatter.format(flowerPrice))
            }
            else{
                flowerPrices.add(formatter.format(flowerPrice))
            }

            if (flowerTribute != null) {
                flowerTributePrice =
                    (flowerTribute.retailPrice.toInt() * flowerTribute.bunch) * flowerTributeOrder!!.extra__bunchCnt!!

                // 포장이 선택되었다면 가격에 3을 곱한다.
                if (flowerTributeOrder.extra__packing == true) {
                    flowerTributePrice *= 3
                }
                sum += flowerTributePrice
                flowerTributePrices.add(formatter.format(flowerTributePrice))
            }
            else{
                flowerTributePrices.add(formatter.format(flowerTributePrice))
            }

            if (femaleMourningClothBlack != null) {
                femaleMourningClothBlackPrice =
                    femaleMourningClothBlack.retailPrice.toInt() * femaleMourningClothBlackOrder!!.extra__femaleClothBlackCnt!!
                sum += femaleMourningClothBlackPrice
                femaleMourningClothBlackPrices.add(formatter.format(femaleMourningClothBlackPrice))
            }
            else{
                femaleMourningClothBlackPrices.add(formatter.format(femaleMourningClothBlackPrice))
            }

            if (femaleMourningClothWhite != null) {
                femaleMourningClothWhitePrice =
                    femaleMourningClothWhite.retailPrice.toInt() * femaleMourningClothWhiteOrder!!.extra__femaleClothWhiteCnt!!
                sum += femaleMourningClothWhitePrice
                femaleMourningClothWhitePrices.add(formatter.format(femaleMourningClothWhitePrice))
            }
            else{
                femaleMourningClothWhitePrices.add(formatter.format(femaleMourningClothWhitePrice))
            }

            if (maleMourningCloth != null) {
                maleMourningClothPrice =
                    maleMourningCloth.retailPrice.toInt() * maleMourningClothOrder!!.extra__maleClothCnt!!
                sum += maleMourningClothPrice
                maleMourningClothPrices.add(formatter.format(maleMourningClothPrice))
            }
            else{
                maleMourningClothPrices.add(formatter.format(maleMourningClothPrice))
            }

            if (shirt != null) {
                shirtPrice =
                    shirt.retailPrice.toInt() * shirtOrder!!.extra__shirtCnt!!
                sum += shirtPrice
                shirtPrices.add(formatter.format(shirtPrice))
            }
            else{
                shirtPrices.add(formatter.format(shirtPrice))
            }

            if (necktie != null) {
                necktiePrice =
                    necktie.retailPrice.toInt() * necktieOrder!!.extra__necktieCnt!!
                sum += necktiePrice
                necktiePrices.add(formatter.format(necktiePrice))
            }
            else{
                necktiePrices.add(formatter.format(necktiePrice))
            }

            if (shroud != null) {
                shroudPrice = shroud.retailPrice.toInt()
                sum += shroudPrice
                shroudPrices.add(formatter.format(shroudPrice))
            }
            else{
                shroudPrices.add(formatter.format(shroudPrice))
            }

            if (coffin != null) {
                coffinPrice = coffin.retailPrice.toInt()
                sum += coffinPrice
                coffinPrices.add(formatter.format(coffinPrice))
            }
            else{
                coffinPrices.add(formatter.format(coffinPrice))
            }

            // 장례별 총 사용액을 배열에 넣어줌
            sums.add(formatter.format(sum))

            // 장례별 고인과 유가족의 정보를 배열에넣어줌
            val client = clientService.getClientById(funeral.clientId)

            if (client != null) {
                clients.add(client)
            }

            val chief = clientService.getFamilyByClientId(funeral.clientId)

            if (chief != null) {
                chiefs.add(chief)
            }
        }

        model.addAttribute("filteredFunerals", filteredFunerals)
        model.addAttribute("clients", clients)
        model.addAttribute("chiefs", chiefs)
        // 페이징 정보
        model.addAttribute("page", page)
        model.addAttribute("totalPage", totalPage)
        // 가격
        model.addAttribute("flowerPrices", flowerPrices)
        model.addAttribute("flowerTributePrices", flowerTributePrices)
        model.addAttribute("femaleMourningClothBlackPrices", femaleMourningClothBlackPrices)
        model.addAttribute("femaleMourningClothWhitePrices", femaleMourningClothWhitePrices)
        model.addAttribute("maleMourningClothPrices", maleMourningClothPrices)
        model.addAttribute("shirtPrices", shirtPrices)
        model.addAttribute("necktiePrices", necktiePrices)
        model.addAttribute("shroudPrices", shroudPrices)
        model.addAttribute("coffinPrices", coffinPrices)
        model.addAttribute("sums", sums)
        // 스탠다드
        model.addAttribute("flowers", flowers)
        model.addAttribute("flowerTributes", flowerTributes)
        model.addAttribute("femaleMourningClothBlacks", femaleMourningClothBlacks)
        model.addAttribute("femaleMourningClothWhites", femaleMourningClothWhites)
        model.addAttribute("maleMourningCloths", maleMourningCloths)
        model.addAttribute("shirts", shirts)
        model.addAttribute("neckties", neckties)
        model.addAttribute("shrouds", shrouds)
        model.addAttribute("coffins", coffins)
        // 주문정보
        model.addAttribute("flowerOrders", flowerOrders)
        model.addAttribute("flowerTributeOrders", flowerTributeOrders)
        model.addAttribute("femaleMourningClothBlackOrders", femaleMourningClothBlackOrders)
        model.addAttribute("femaleMourningClothWhiteOrders", femaleMourningClothWhiteOrders)
        model.addAttribute("maleMourningClothOrders", maleMourningClothOrders)
        model.addAttribute("shirtOrders", shirtOrders)
        model.addAttribute("necktieOrders", necktieOrders)
        model.addAttribute("shroudOrders", shroudOrders)
        model.addAttribute("coffinOrders", coffinOrders)
        // 운구차 정보
        model.addAttribute("coffinTransporters", coffinTransporters)
        model.addAttribute("coffinTransporterMembers", coffinTransporterMembers)
        model.addAttribute("coffinTransporterCellphoneNos", coffinTransporterCellphoneNos)

        return "usr/director/progress"
    }

    @RequestMapping("/usr/director/modifyFuneral")
    fun showModifyFuneral(
        model: Model,
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {


        // 파라미터가 자동으로 입력되는 정상적인 접근이 아닌 경우에 대한 예외처리
        if (clientId == 0) {
            return "usr/home/main"
        }

        val funeral = clientService.getFuneralByClientId(clientId)
        // 잘못된 clientId 파라미터로 접근하는 경우에 대한 예외처리
        if (funeral == null) {
            return "usr/home/main"
        }

        val client = clientService.getClientById(clientId)

        // 해당 페이지에 접근한 장례지도사가 조회한 장례를 진행하는 장례지도사가 아닌 경우에 대한 예외처리
        if (rq.getLoginedMember()!!.id != funeral.directorMemberId || client == null) {
            return "usr/home/main"
        }


        // 상주 정보 불러옴
        val chief = clientService.getFamilyByClientId(clientId)
        // 저장된 유가족 정보를 불러옴 (상주는 제외)
        val familyMembers = clientService.getFamilyMembersByClientId(clientId)

        val departments = memberService.getDepartments()

        // client 테이블의 funeralHall 칼럼에 값이 있는 경우에만 실행
        // client.funeralHall의 값은 운구차 호출 시, 장례 정보 수정 및 입력 페이지에서 입력 시, 들어간다.
        if(client.funeralHall != null && client.funeralHall.isNotBlank()){

            val funeralHall = memberRoleService.getFuneralHallByName2(client.funeralHall)
            // 운구차 호출에서 직접 입력을 했을 때, 장례식장이 아닌 경우, 위 funeralHall은 조회되지 않는다.
            if(funeralHall != null){

                // client 테이블의 funeralHall에 값이 있는 경우, 해당 장례식장 관련 정보를 불러온다.
                model.addAttribute("departmentName", funeralHall.department)
                model.addAttribute("departmentDetailSelected", funeralHall.departmentDetail)
                model.addAttribute("funeralHallNameSelected", funeralHall.name)

                val departmentDetails = memberRoleService.getDepartmentDetailsByDepartment(funeralHall.department)
                model.addAttribute("departmentDetails", departmentDetails)

                val funeralHallNames = memberRoleService.getFuneralHallNamesByDepartmentDetail(funeralHall.departmentDetail)
                model.addAttribute("funeralHallNames", funeralHallNames)
            }
        }


        val banks = memberService.getBanks()

        model.addAttribute("banks", banks)
        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("chief", chief)
        model.addAttribute("familyMembers", familyMembers)
        model.addAttribute("departments", departments)


        return "usr/director/modifyFuneral"
    }

    @RequestMapping("/usr/director/doModifyFuneral")
    @ResponseBody
    fun doModifyFuneral(
            @RequestParam(defaultValue = "") funeralHallName: String,
            @RequestParam(defaultValue = "") funeralHallRoom: String,
            @RequestParam(defaultValue = "") deceasedName: String,
            @RequestParam(defaultValue = "") frontNum: String,
            @RequestParam(defaultValue = "") backNum: String,
            @RequestParam(defaultValue = "") deceasedHomeAddress: String,
            @RequestParam(defaultValue = "") familyClan: String,
            @RequestParam(defaultValue = "") religion: String,
            @RequestParam(defaultValue = "") duty: String,
            @RequestParam(defaultValue = "") birth: String,
            @RequestParam(defaultValue = "") deceasedDate: String,

            @RequestParam(defaultValue = "N") sex: Char,
            @RequestParam(defaultValue = "N") birthLunar: Char,
            @RequestParam(defaultValue = "N") deceasedLunar: Char,
            @RequestParam(defaultValue = "N") funeralMethod: Char,

            @RequestParam(defaultValue = "") cremationLocation: String,
            @RequestParam(defaultValue = "") buryLocation: String,

            @RequestParam(defaultValue = "0") cause: String,
            @RequestParam(defaultValue = "N") papers: Char,
            @RequestParam(defaultValue = "N") autopsyCheck: Char,

            @RequestParam(defaultValue = "") casketDate: String,
            @RequestParam(defaultValue = "") casketTime: String,
            @RequestParam(defaultValue = "") leavingDate: String,
            @RequestParam(defaultValue = "") leavingTime: String,
            @RequestParam(defaultValue = "") chiefName: String,
            @RequestParam(defaultValue = "") chiefRelation: String,
            @RequestParam(defaultValue = "") chiefCellphoneNo: String,
            @RequestParam(defaultValue = "") chiefAddress: String,
            @RequestParam(defaultValue = "") chiefAccountNum: String,
            @RequestParam(defaultValue = "") chiefBank: String,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String{

        return Ut.getJsonStrFromObj(clientService.modifyFuneral(funeralHallName, funeralHallRoom, deceasedName, frontNum, backNum, deceasedHomeAddress, familyClan, religion, duty, birth, deceasedDate, birthLunar, deceasedLunar, funeralMethod, cremationLocation, buryLocation, cause, papers, autopsyCheck, casketDate, casketTime, leavingTime, leavingDate, chiefName, chiefRelation, chiefCellphoneNo, chiefAddress, clientId, sex, chiefAccountNum, chiefBank))
    }

    @RequestMapping("/usr/director/addFamily")
    @ResponseBody
    fun addFamily(
            @RequestParam(defaultValue = "") familyRelation: String,
            @RequestParam(defaultValue = "") familyName: String,
            @RequestParam(defaultValue = "") familyCellphoneNo: String,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String{

        // 유가족 추가 시, Ajax로 이 controller를 실행
        return Ut.getJsonStrFromObj(clientService.addFamily(familyRelation, familyName, familyCellphoneNo, clientId))
    }

    @RequestMapping("/usr/director/removeFamily")
    @ResponseBody
    fun removeFamily(
            @RequestParam(defaultValue = "") familyRelation: String,
            @RequestParam(defaultValue = "") familyName: String,
            @RequestParam(defaultValue = "") familyCellphoneNo: String,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String{

        // 유가족 삭제 시, Ajax로 이 controller를 실행
        return Ut.getJsonStrFromObj(clientService.removeFamily(familyRelation, familyName, familyCellphoneNo, clientId))
    }


    @RequestMapping("/usr/director/departmentDetail", method = [RequestMethod.POST])
    @ResponseBody
    fun departmentDetail(
            @RequestParam(defaultValue = "") department: String
    ): String {


        return Ut.getJsonStrFromObj(memberRoleService.getFuneralHallsByDepartment(department.trim()))

    }

    @RequestMapping("/usr/director/funeralHallName", method = [RequestMethod.POST])
    @ResponseBody
    fun funeralHallName(
            @RequestParam(defaultValue = "") departmentDetail: String
    ): String{

        return Ut.getJsonStrFromObj(memberRoleService.getFuneralHallsByDepartmentDetail(departmentDetail.trim()))
    }

    @RequestMapping("/usr/director/funeralHallNum", method = [RequestMethod.POST])
    @ResponseBody
    fun funeralHallNum(
            @RequestParam(defaultValue = "") name: String
    ): String{
        return Ut.getJsonStrFromObj(memberRoleService.getFuneralHallByName(name.trim()))
    }

    @RequestMapping("/usr/director/selectFlower")
    fun showSelectFlower(
        model: Model,
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        val funeral = clientService.getFuneralByClientId(clientId)

        val flowers = vendorService.getFlowers()
        val flowerTributes = vendorService.getFlowerTributes()

        if (funeral != null) {
            val details = mutableListOf<String>()
            details.add("flower")
            details.add("flowerTribute")

            for(detail in details){
                val order = vendorService.getOrderByFuneralIdAndCompletionStatusAndDetail(
                    funeral.id,
                    false,
                    detail
                )

                model.addAttribute(detail + "Order", order)
            }
        }


        model.addAttribute("flowerTributes", flowerTributes)
        model.addAttribute("flowers", flowers)
        model.addAttribute("funeral", funeral)

        return "usr/director/selectFlower"
    }

    @RequestMapping("/usr/director/selectCoffinTransporter")
    fun selectCoffinTransporter(
            model: Model,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String {


        val client = clientService.getClientById(clientId)
        val funeral = clientService.getFuneralByClientId(clientId)
        val chief = clientService.getFamilyByClientId(clientId)


        val clientCellphoneNo = Ut.getCellphoneNoFormatted(chief.cellphoneNo)

        val departments = memberService.getDepartments()

        // client, funeral, chief이 null인 경우, 잘못된 clienId로 접근한 경우이므로 돌려보냄
        if(funeral == null || client == null || chief == null){
            return "usr/home/main"
        }
        // 운구차를 이미 호출한 경우, 다시 호출할 수 없다.
        if(funeral.coffinTransporterUseStatus){
            return "redirect:/usr/director/progress?clientId=$clientId"
        }

        // 담당 장례지도사 id와 현재 로그인한 사람의 id가 일치하는지 검사
        if(funeral.directorMemberId != rq.getLoginedMember()!!.id){
            return "usr/home/main"
        }

        val coffinTransporter = clientService.getCoffinTransporterByFuneralId(funeral.id)

        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("chief", chief)
        model.addAttribute("clientCellphoneNo", clientCellphoneNo)
        model.addAttribute("departments", departments)
        model.addAttribute("coffinTransporter", coffinTransporter)

        return "usr/director/selectCoffinTransporter"
    }

    @RequestMapping("/usr/director/selectMourningCloth")
    fun showSelectMourningCloth(
        model: Model,
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        val funeral = clientService.getFuneralByClientId(clientId)

        // 각 상품의 스탠다드 데이터들을 DB에서 조회
        val femaleMourningClothBlacks = vendorService.getFemaleMourningClothBlacks()
        val femaleMourningClothWhites = vendorService.getFemaleMourningClothWhites()
        val maleMourningCloths = vendorService.getMaleMourningCloths()
        val shirts = vendorService.getShirts()
        val neckties = vendorService.getNeckties()

        if (funeral != null) {
            val details = mutableListOf<String>()
            details.add("femaleMourningClothBlack")
            details.add("femaleMourningClothWhite")
            details.add("maleMourningCloth")
            details.add("shirt")
            details.add("necktie")

            for(detail in details){
                val order = vendorService.getOrderByFuneralIdAndCompletionStatusAndDetail(
                    funeral.id,
                    false,
                    detail
                )

                model.addAttribute(detail + "Order", order)
            }
        }

        model.addAttribute("femaleMourningClothBlacks", femaleMourningClothBlacks)
        model.addAttribute("femaleMourningClothWhites", femaleMourningClothWhites)
        model.addAttribute("maleMourningCloths", maleMourningCloths)
        model.addAttribute("shirts", shirts)
        model.addAttribute("neckties", neckties)

        model.addAttribute("funeral", funeral)

        return "usr/director/selectMourningCloth"
    }

    @RequestMapping("/usr/director/selectShroud")
    fun showSelectShroud(
        model: Model,
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        val funeral = clientService.getFuneralByClientId(clientId)
        val shrouds = vendorService.getShrouds()

        // 관의 이름만
        val coffinNames = vendorService.getCoffinNames()


        if (funeral != null) {
            val client = clientService.getClientById(funeral.clientId)

            model.addAttribute("client", client)

            val details = mutableListOf<String>()
            details.add("shroud")

            for(detail in details){
                val order = vendorService.getOrderByFuneralIdAndCompletionStatusAndDetail(
                    funeral.id,
                    false,
                    detail
                )

                model.addAttribute(detail + "Order", order)
            }
        }

        model.addAttribute("shrouds", shrouds)
        model.addAttribute("funeral", funeral)
        model.addAttribute("coffinNames", coffinNames)

        return "usr/director/selectShroud"
    }

    @RequestMapping("/usr/director/selectMortuary")
    fun showSelectMortuary(
        model: Model,
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        val funeral = clientService.getFuneralByClientId(clientId)

        val incenses = vendorService.getIncenses()

        if (funeral != null) {
            val details = mutableListOf<String>()
            details.add("incense")

            for(detail in details){
                val order = vendorService.getOrderByFuneralIdAndCompletionStatusAndDetail(
                    funeral.id,
                    false,
                    detail
                )

                model.addAttribute(detail + "Order", order)
            }
        }


        model.addAttribute("incenses", incenses)
        model.addAttribute("funeral", funeral)

        return "usr/director/selectMortuary"
    }

    @RequestMapping("/usr/director/dispatch")
    fun showDispatch(
        model: Model,
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {

        // 존재하지 않는 clientId를 URL로 접근하는 경우에 대한 예외처리
        val client = clientService.getClientById(clientId)

        if (client == null) {
            return "usr/home/main"
        }

        val chief = clientService.getFamilyByClientId(clientId)

        model.addAttribute("chief", chief)
        model.addAttribute("client", client)


        return "usr/director/dispatch"
    }


    @RequestMapping("/usr/director/doDispatch", method = [RequestMethod.POST])
    @ResponseBody
    fun doDispatch(
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        return Ut.getJsonStrFromObj(
            clientService.modifyClientIntoDirectorMemberIdByClientId(
                rq.getLoginedMember()!!.id,
                clientId
            )
        )
    }


    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/usr/director/doRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doRequest(
        introduce: String,
        multipartRequest: MultipartRequest
    ): String {
        // Request 페이지에서 넘어온 파라미터를 DB에 추가하는 과정
        // 장례지도사 승인 신청 시, roleLevel(=roleId)는 3(장례지도사)가 되고, authenticationStatus에 따라서 이후 구분된다.

        val fileMap = multipartRequest.fileMap
        for (fileInputName in fileMap.keys) {
            val multipartFile = fileMap[fileInputName]

            if (multipartFile != null) {
                // 파일을 저장
                genFileService.save(multipartFile, rq.getLoginedMember()!!.id)
            }
        }

        //  장례지도사의 roleLevel(roleId)는 3

        // memberRole 테이블에 유저가 입력한 자기소개 및 회원번호, roleLevel(roleId) 을 3으로 데이터를 생성
        memberRoleService.insertMemberRole(introduce, rq.getLoginedMember()!!.id, 3, 0)

        // roleLevel을 3(장례지도사) 으로 변경
        memberService.modifyMemberIntoRoleLevelByMemberId(rq.getLoginedMember()!!.id, 3)

        // requestStatus 칼럼을 true로 변경
        memberService.modifyMemberIntoRequestStatusByMemberId(rq.getLoginedMember()!!.id, true)

        // 장례지도사 신청되면 회원정보가 갱신됨으로 현재 로그인중인 사용자의 세션또한 갱신해준다
        rq.login(memberService.getMemberById(rq.getLoginedMember()!!.id)!!)

        return rq.replaceJs("장례지도사 영업신청이 완료되었습니다.", "../home/main")
    }

    @RequestMapping("/usr/director/doSelectCoffinTransporter", method = [RequestMethod.POST])
    @ResponseBody
    fun doSelectCoffinTransporter(
        @RequestParam(defaultValue = "0") clientId: Int,
        @RequestParam(defaultValue = "0") funeralId: Int,
        @RequestParam(defaultValue = "") deceasedName: String,
        @RequestParam(defaultValue = "N") sex: String,
        @RequestParam(defaultValue = "") frontNum: String,
        @RequestParam(defaultValue = "") backNum: String,
        @RequestParam(defaultValue = "") deceasedHomeAddress: String,
        @RequestParam(defaultValue = "") departureAddress: String,
        @RequestParam(defaultValue = "") destinationAddress: String,
        @RequestParam(defaultValue = "") funeralHallName: String,
        @RequestParam(defaultValue = "") department: String
    ): String {

        return Ut.getJsonStrFromObj(
            vendorService.callCoffinTransporter(clientId, funeralId, deceasedName, sex, frontNum, backNum, deceasedHomeAddress, departureAddress, destinationAddress, funeralHallName, department))

    }

    @RequestMapping("/usr/director/doSelectMourningCloth", method = [RequestMethod.POST])
    @ResponseBody
    fun doSelectMourningCloth(
        funeralId: Int,
        @RequestParam(defaultValue = "0") femaleMourningClothBlackId: Int,
        @RequestParam(defaultValue = "0") femaleMourningClothBlackCnt: Int,
        @RequestParam(defaultValue = "0") femaleMourningClothWhiteId: Int,
        @RequestParam(defaultValue = "0") femaleMourningClothWhiteCnt: Int,
        @RequestParam(defaultValue = "0") maleMourningClothId: Int,
        @RequestParam(defaultValue = "0") maleClothCnt: Int,
        @RequestParam(defaultValue = "0") shirtId: Int,
        @RequestParam(defaultValue = "0") shirtCnt: Int,
        @RequestParam(defaultValue = "0") necktieId: Int,
        @RequestParam(defaultValue = "0") necktieCnt: Int
    ): String {
        return Ut.getJsonStrFromObj(
            vendorService.modifyFuneralIntoMourningClothId(
                funeralId,
                femaleMourningClothBlackId,
                femaleMourningClothBlackCnt,
                femaleMourningClothWhiteId,
                femaleMourningClothWhiteCnt,
                maleMourningClothId,
                maleClothCnt,
                shirtId,
                shirtCnt,
                necktieId,
                necktieCnt
            )
        )
    }

    @RequestMapping("/usr/director/doSelectMortuary", method = [RequestMethod.POST])
    @ResponseBody
    fun doSelectMortuary(
        funeralId: Int,
        @RequestParam(defaultValue = "0") incenseId: Int,
        @RequestParam(defaultValue = "0") incenseCnt: Int
    ): String {
        return Ut.getJsonStrFromObj(
            vendorService.modifyFuneralIntoMortuaryIds(
                funeralId,
                incenseId,
                incenseCnt
            )
        )
    }

    @RequestMapping("/usr/director/doSelectFlower", method = [RequestMethod.POST])
    @ResponseBody
    fun doSelectFlower(
        funeralId: Int,
        @RequestParam(defaultValue = "0") flowerId: Int,
        @RequestParam(defaultValue = "0") flowerTributeId: Int,
        @RequestParam(defaultValue = "0") bunchCnt: Int,
        @RequestParam(defaultValue = "N") packing: Char
    ): String {
        return Ut.getJsonStrFromObj(
            vendorService.modifyFuneralIntoFlowerId(
                funeralId,
                flowerId,
                flowerTributeId,
                bunchCnt,
                packing
            )
        )
    }

    @RequestMapping("/usr/director/doSelectShroud", method = [RequestMethod.POST])
    @ResponseBody
    fun doSelectShroud(
        funeralId: Int,
        @RequestParam(defaultValue = "0") shroudId: Int,
        @RequestParam(defaultValue = "0") coffinId: Int
    ): String {



        return Ut.getJsonStrFromObj(
            vendorService.modifyFuneralIntoShroudId(
                funeralId,
                shroudId,
                coffinId
            )
        )
    }

    @RequestMapping("/usr/director/moveProgress", method = [RequestMethod.POST])
    @ResponseBody
    fun doMoveProgressPage(
    ): String {
        val funeral = clientService.getProgressingFuneralByDirectorMemberId(rq.getLoginedMember()!!.id)

        if (funeral == null) {
            return Ut.getJsonStrFromObj(clientService.moveProgressRd(0))
        }

        return Ut.getJsonStrFromObj(clientService.moveProgressRd(funeral.clientId))
    }


    // VIEW 기능 함수 끝

    @RequestMapping("/usr/director/coffinChi", method = [RequestMethod.POST])
    @ResponseBody
    fun coffinChi(
            @RequestParam(defaultValue = "") coffinName: String
    ): String {


        return Ut.getJsonStrFromObj(memberRoleService.getCoffinChisByName(coffinName.trim()))

    }

    @RequestMapping("/usr/director/coffinSize", method = [RequestMethod.POST])
    @ResponseBody
    fun coffinSize(
            @RequestParam(defaultValue = "") coffinChi: String
    ): String {


        return Ut.getJsonStrFromObj(memberRoleService.getCoffinSizesByChi(coffinChi.trim()))

    }

    @RequestMapping("/usr/director/getCoffin", method = [RequestMethod.POST])
    @ResponseBody
    fun getCoffinId(
            @RequestParam(defaultValue = "") coffinName: String,
            @RequestParam(defaultValue = "") coffinChi: String,
            @RequestParam(defaultValue = "") coffinSize: String
    ): String {


        return Ut.getJsonStrFromObj(memberRoleService.getCoffinByAll(coffinName.trim(), coffinChi.trim(), coffinSize.trim()))

    }

}