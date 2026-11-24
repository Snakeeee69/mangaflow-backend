package com.mangaflow.catalogo.config;

import com.mangaflow.catalogo.model.FormatoManga;
import com.mangaflow.catalogo.model.Manga;
import com.mangaflow.catalogo.repository.MangaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatalogoDataInitializer implements CommandLineRunner {

    private final MangaRepository mangaRepository;

    public CatalogoDataInitializer(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    @Override
    public void run(String... args) {
        if (mangaRepository.count() > 0) return;

        mangaRepository.saveAll(List.of(
                // === FÍSICO Y DIGITAL (12 obras) ===
                Manga.builder().titulo("Ataque a los Titanes").autor("Hajime Isayama").editorial("Norma Editorial")
                        .genero("Acción, Drama, Fantasy").demografia("Shōnen")
                        .sinopsis("Eren Jaeger se une al cuerpo de exploración para exterminar a los titanes que destruyeron su hogar y descubrir la verdad del mundo.")
                        .numeroTomo(34).precioFisico(9490.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/ataque_a_los_titanes_1.jpg")
                        .stock(15).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Atom: The Beginning").autor("Tetsuro Kasahara, Makoto Tezuka y Masami Yuki").editorial("Milky Way Ediciones")
                        .genero("Acción, Histórico").demografia("Seinen")
                        .sinopsis("Precuela de Astro Boy sobre los jóvenes científicos Tenma y Ochanomizu en la creación del robot A106.")
                        .numeroTomo(21).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/Atom_the_beginning_1.png")
                        .stock(12).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Berserk").autor("Kentaro Miura").editorial("Panini Manga")
                        .genero("Acción, Horror, Fantasy, Drama").demografia("Seinen")
                        .sinopsis("Guts, un mercenario traumatizado, empuña una enorme espada en busca de venganza contra su antiguo amigo Griffith en un mundo oscuro.")
                        .numeroTomo(42).precioFisico(11990.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/Berserk.png")
                        .stock(8).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Demon Slayer").autor("Koyoharu Gotouge").editorial("Ivrea Argentina")
                        .genero("Acción, Aventura, Fantasy").demografia("Shōnen")
                        .sinopsis("Tanjiro se convierte en cazador de demonios para curar a su hermana Nezuko, transformada en monstruo tras la masacre de su familia.")
                        .numeroTomo(23).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/Demon_Slayer_01.jpg")
                        .stock(20).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Kaguya-sama: Love is War").autor("Aka Akasaka").editorial("Ivrea Argentina")
                        .genero("Romance, Comedia, Drama").demografia("Seinen")
                        .sinopsis("Dos prodigios del consejo estudiantil enamorados recurren a batallas de ingenio psicológico para obligar al otro a confesarse primero.")
                        .numeroTomo(28).precioFisico(9490.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/Kaguya_sama_arg_01.jpg")
                        .stock(14).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Las Quintillizas").autor("Negi Haruba").editorial("Norma Editorial")
                        .genero("Romance, Comedia").demografia("Shōnen")
                        .sinopsis("Futaro, un estudiante pobre pero genio, se convierte en tutor privado de cinco hermanas quintillizas idénticas para ayudarlas a graduarse.")
                        .numeroTomo(14).precioFisico(9990.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/Las_quintillizas_1.png")
                        .stock(11).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Naruto").autor("Masashi Kishimoto").editorial("Panini Manga")
                        .genero("Acción, Aventura").demografia("Shōnen")
                        .sinopsis("Naruto, un joven ninja marginado con un demonio zorro dentro, sueña con convertirse en Hokage para ser reconocido por su aldea.")
                        .numeroTomo(72).precioFisico(8590.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/naruto_tomo_1.png")
                        .stock(18).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("One Piece").autor("Eiichiro Oda").editorial("Ivrea Argentina")
                        .genero("Acción, Aventura, Fantasy").demografia("Shōnen")
                        .sinopsis("Monkey D. Luffy y su tripulación de piratas navegan por el mundo buscando el tesoro One Piece para convertirse en el Rey de los Piratas.")
                        .numeroTomo(110).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/One_Piece_01.jpg")
                        .stock(25).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Spy x Family").autor("Tatsuya Endo").editorial("Ivrea Argentina")
                        .genero("Acción, Comedia").demografia("Shōnen")
                        .sinopsis("Un espía, una asesina a sueldo y una niña telépata forman una familia falsa sin revelarse sus verdaderas identidades para cumplir una misión.")
                        .numeroTomo(14).precioFisico(8590.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/Spy_x_family_1.png")
                        .stock(19).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Vinland Saga").autor("Makoto Yukimura").editorial("Planeta Cómic")
                        .genero("Acción, Histórico, Drama").demografia("Seinen")
                        .sinopsis("Thorfinn busca venganza tras la muerte de su padre con la banda de su asesino, en una travesía nórdica hacia la madurez y la paz.")
                        .numeroTomo(28).precioFisico(10990.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/vinland_saga.png")
                        .stock(10).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Jujutsu Kaisen").autor("Gege Akutami").editorial("Panini Manga")
                        .genero("Acción, Horror, Fantasy").demografia("Shōnen")
                        .sinopsis("Yuji Itadori ingiere un talismán maldito para salvar a sus amigos y entra al mundo de los hechiceros para erradicar maldiciones.")
                        .numeroTomo(28).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/xJujutsu_kaisen_arg_01.jpg")
                        .stock(14).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Call of the Night").autor("Kotoyama").editorial("Ivrea Argentina")
                        .genero("Romance, Comedia, Psicológico").demografia("Shōnen")
                        .sinopsis("Kou, un estudiante con insomnio, vaga de noche y conoce a la vampiresa Nazuna, decidiendo enamorarse de ella para transformarse en vampiro.")
                        .numeroTomo(20).precioFisico(8590.0).precioDigital(2300.0)
                        .formato(FormatoManga.FISICO_Y_DIGITAL).imagen("/images/catalogo/yofukashi_no_uta.jpg")
                        .stock(13).proximamenteFisico(false).activo(true).build(),

                // === SOLO DIGITAL (10 obras) ===
                Manga.builder().titulo("Bakemonogatari").autor("Nisio Isin y Oh! great").editorial("Milky Way Ediciones")
                        .genero("Romance, Psicológico").demografia("Seinen")
                        .sinopsis("Koyomi Araragi, un estudiante traumatizado tras sobrevivir a un vampiro, ayuda a chicas afectadas por diversas entidades sobrenaturales.")
                        .numeroTomo(22).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/catalogo/Bakemonogatari_1.png")
                        .stock(0).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Demon Slave").autor("Takahiro y Yohei Takemura").editorial("Norma Editorial")
                        .genero("Acción, Fantasy").demografia("Shōnen")
                        .sinopsis("Yuki queda atrapado en una dimensión plagada de monstruos y firma un pacto de servidumbre con la capitana Kyouka a cambio de poder.")
                        .numeroTomo(16).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/catalogo/Demon_slave_01.jpg")
                        .stock(0).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Frieren").autor("Kanehito Yamada").editorial("Ivrea Argentina")
                        .genero("Aventura, Fantasy, Drama").demografia("Shōnen")
                        .sinopsis("Tras derrotar al Rey Demonio, la elfa inmortal Frieren emprende un viaje para aprender a valorar los vínculos y sentimientos humanos.")
                        .numeroTomo(13).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/catalogo/Frieren_01.jpg")
                        .stock(0).proximamenteFisico(true).activo(true).build(),

                Manga.builder().titulo("KonoSuba").autor("Natsume Akatsuki y Masahito Watari").editorial("Ivrea Argentina")
                        .genero("Comedia, Fantasy, Aventura").demografia("Shōnen")
                        .sinopsis("Tras morir de forma ridícula, Kazuma reencarna en un mundo fantástico junto a una diosa torpe, formando un insólito grupo de aventureros.")
                        .numeroTomo(19).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/catalogo/Konosuba_arg_01.jpg")
                        .stock(0).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Oshi no Ko").autor("Aka Akasaka y Mengo Yokoyari").editorial("Ivrea Argentina")
                        .genero("Drama, Psicológico").demografia("Seinen")
                        .sinopsis("Un médico reencarna como hijo de su idol favorita y se adentra en la industria del espectáculo para vengar un traumático crimen.")
                        .numeroTomo(16).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/catalogo/Oshi_no_ko_arg_01.jpg")
                        .stock(0).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Akane-banashi").autor("Yuki Suenaga y Takamasa Moue").editorial("Milky Way Ediciones")
                        .genero("Drama, Comedia").demografia("Shōnen")
                        .sinopsis("Akane se adentra en el tradicional arte escénico del rakugo para reivindicar a su padre y alcanzar el máximo rango de la disciplina.")
                        .numeroTomo(13).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/mangas_digitales/akane_banashi.jpg")
                        .stock(0).proximamenteFisico(true).activo(true).build(),

                Manga.builder().titulo("Dandadan").autor("Yukinobu Tatsu").editorial("Ivrea Argentina")
                        .genero("Acción, Horror, Comedia").demografia("Shōnen")
                        .sinopsis("Momo y Okarun unen fuerzas tras descubrir que tanto los fantasmas como los alienígenas son reales.")
                        .numeroTomo(16).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/mangas_digitales/Dandadan.jpg")
                        .stock(0).proximamenteFisico(true).activo(true).build(),

                Manga.builder().titulo("Hunter x Hunter").autor("Yoshihiro Togashi").editorial("Panini Manga")
                        .genero("Acción, Aventura, Fantasy").demografia("Shōnen")
                        .sinopsis("Gon Freecss realiza el peligroso examen de Cazador para encontrar a su padre y adentrarse en un mundo repleto de misterios y desafíos.")
                        .numeroTomo(38).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/mangas_digitales/hunter_x_hunter.jpg")
                        .stock(0).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("RuriDragon").autor("Masaoki Shindo").editorial("Planeta Cómic")
                        .genero("Comedia, Fantasy").demografia("Shōnen")
                        .sinopsis("Ruri, una estudiante común, descubre de golpe que su padre es un dragón y debe adaptarse a su vida escolar mientras le crecen cuernos y exhala fuego.")
                        .numeroTomo(2).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/mangas_digitales/RuriDragon.jpg")
                        .stock(0).proximamenteFisico(false).activo(true).build(),

                Manga.builder().titulo("Sakamoto Days").autor("Yuto Suzuki").editorial("Ivrea Argentina")
                        .genero("Acción, Comedia").demografia("Shōnen")
                        .sinopsis("Taro Sakamoto, el legendario asesino a sueldo ya retirado y dedicado a su familia, debe proteger su tienda de abarrotes de antiguos colegas.")
                        .numeroTomo(18).precioFisico(8990.0).precioDigital(2300.0)
                        .formato(FormatoManga.DIGITAL).imagen("/images/mangas_digitales/SAKAMOTO_DAYS.jpg")
                        .stock(0).proximamenteFisico(true).activo(true).build()
        ));
    }
}